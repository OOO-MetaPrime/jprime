package mp.jprime.schedule.loaders.xml.services;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.parsers.ParserService;
import mp.jprime.schedule.*;
import mp.jprime.schedule.loaders.xml.JpScheduleTaskXmlResources;
import mp.jprime.schedule.loaders.xml.beans.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Основная загрузка задач, описанных через xml
 */
@Service
public final class JpScheduleTaskXmlLoader implements JpScheduleTaskLoader {
  private static final Logger LOG = LoggerFactory.getLogger(JpScheduleTaskXmlLoader.class);

  private final ParserService parserService;
  private final JpScheduleExecutorStorage executorStorage;
  private final Collection<JpScheduleTaskXmlResources> resources;

  private JpScheduleTaskXmlLoader(@Autowired ParserService parserService,
                                  @Autowired JpScheduleExecutorStorage executorStorage,
                                  @Autowired(required = false) Collection<JpScheduleTaskXmlResources> resources) {
    this.parserService = parserService;
    this.executorStorage = executorStorage;
    this.resources = resources;
  }

  @Override
  public Collection<JpScheduleTask> getTasks() {
    if (resources == null || resources.isEmpty()) {
      return Collections.emptyList();
    }
    Collection<JpScheduleTask> result = new ArrayList<>();
    for (JpScheduleTaskXmlResources resource : resources) {
      result.addAll(xmlLoad(resource.getResources()));
    }
    return result;
  }

  private Collection<JpScheduleTask> xmlLoad(Collection<Resource> resources) {
    if (resources == null || resources.isEmpty()) {
      return Collections.emptyList();
    }
    try {
      XmlMapper xmlMapper = new XmlMapper();

      Collection<JpScheduleTask> result = new ArrayList<>();
      for (Resource res : resources) {
        try (InputStream is = res.getInputStream()) {
          XmlJpTaskSettings taskSettings = xmlMapper.readValue(is, XmlJpTaskSettings.class);
          if (taskSettings == null) {
            continue;
          }
          XmlJpTasks jpTasks = taskSettings.getJpTasks();
          XmlJpTask[] xmlJpTaskArr = jpTasks != null ? jpTasks.getJpTask() : null;
          if (xmlJpTaskArr != null) {
            for (XmlJpTask xmlJpTask : xmlJpTaskArr) {
              if (xmlJpTask.isDisable()) {
                continue;
              }
              JpScheduleTask task = toTask(xmlJpTask);
              if (task == null) {
                continue;
              }
              result.add(task);
            }
          }
        }
      }
      return result;
    } catch (IOException e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  private JpScheduleTask toTask(XmlJpTask xml) {
    UUID code = xml.getCode();
    String name = xml.getName();
    String description = xml.getDescription();
    String catalogCode = xml.getCatalogCode();
    String executorCode = xml.getExecutorCode();
    XmlParamValues xmlParamValues = xml.getParamValues();
    XmlParamValue[] xmlParamValueArr = xmlParamValues != null ? xmlParamValues.getParamValue() : null;
    XmlJpCron jpCron = xml.getJpCron();
    String jpCronExpression = jpCron != null ? jpCron.getExpression() : null;

    JpScheduleExecutor executor = executorStorage.getExecutor(executorCode);
    if (executor == null) {
      LOG.error("JpScheduleExecutor with code {} not found", executorCode);
      return null;
    }

    Map<String, String> paramMap = new HashMap<>();
    if (xmlParamValueArr != null) {
      for (XmlParamValue paramValue : xmlParamValueArr) {
        paramMap.put(paramValue.getCode(), paramValue.getValue());
      }
    }

    Map<String, Object> paramValues = new HashMap<>();
    executor.getParams().forEach(x -> {
      String paramCode = x.getCode();
      paramValues.put(paramCode, parserService.parseTo(x.getType().getJavaClass(), paramMap.get(paramCode)));
    });

    return JpScheduleTask.of(
        code, name, description, catalogCode, executorCode,
        JPData.of(paramValues), JpScheduleCron.of(jpCronExpression)
    );
  }
}