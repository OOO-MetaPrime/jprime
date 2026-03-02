package mp.jprime.utils.loaders.xml.services;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import mp.jprime.application.JPApplicationShutdownManager;
import mp.jprime.common.JPAppendType;
import mp.jprime.common.JPParam;
import mp.jprime.dataaccess.templatevalues.JPTemplateValueService;
import mp.jprime.dataaccess.transaction.ChainedTransactionManager;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.parsers.ParserService;
import mp.jprime.utils.*;
import mp.jprime.utils.beans.JPUtilModeSettingsBean;
import mp.jprime.utils.beans.JPUtilSettingsBean;
import mp.jprime.utils.loaders.xml.JPUtilXmlAction;
import mp.jprime.utils.loaders.xml.JPUtilXmlOperation;
import mp.jprime.utils.loaders.xml.JPUtilXmlResources;
import mp.jprime.utils.loaders.xml.beans.*;
import mp.jprime.xml.beans.XmlParam;
import mp.jprime.xml.beans.XmlParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Основная загрузка утилит, описанных через xml
 */
@Service
public final class JPUtilXmlLoader implements JPUtilLoader {
  private static final Logger LOG = LoggerFactory.getLogger(JPUtilXmlLoader.class);

  private final Collection<JPUtilXmlResources> resources;
  private final Map<String, JPUtilXmlOperation> operations;

  private ChainedTransactionManager transactionManager;
  private final JPApplicationShutdownManager shutdownManager;
  private JPTemplateValueService templateValueService;
  private ParserService parserService;

  private JPUtilXmlLoader(@Autowired JPApplicationShutdownManager shutdownManager,
                          @Autowired(required = false) Collection<JPUtilXmlResources> resources,
                          @Autowired Collection<JPUtilXmlOperation> operations) {
    this.shutdownManager = shutdownManager;
    this.resources = resources;

    Map<String, JPUtilXmlOperation> operationMap = new HashMap<>();
    for (JPUtilXmlOperation operation : operations) {
      String code = operation.getCode();
      if (operationMap.containsKey(code)) {
        LOG.error("Error duplication JPUtilXmlOperation with type {}", code);
        shutdownManager.exitWithError();
      }
      operationMap.put(code, operation);
    }
    this.operations = operationMap;
  }

  @Autowired
  private void setTransactionManager(ChainedTransactionManager transactionManager) {
    this.transactionManager = transactionManager;
  }

  @Autowired
  private void setTemplateValueService(JPTemplateValueService templateValueService) {
    this.templateValueService = templateValueService;
  }

  @Autowired
  private void setParserService(ParserService parserService) {
    this.parserService = parserService;
  }

  @Override
  public Collection<JPUtilSettings> getUtils() {
    if (resources == null || resources.isEmpty()) {
      return Collections.emptyList();
    }
    Collection<JPUtilSettings> result = new ArrayList<>();
    for (JPUtilXmlResources resource : resources) {
      result.addAll(xmlLoad(resource.getResources()));
    }
    return result;
  }

  private Collection<JPUtilSettings> xmlLoad(Collection<Resource> resources) {
    if (resources == null || resources.isEmpty()) {
      return Collections.emptyList();
    }
    try {
      XmlMapper xmlMapper = new XmlMapper();

      Collection<JPUtilSettings> result = new ArrayList<>();
      for (Resource res : resources) {
        try (InputStream is = res.getInputStream()) {
          XmlJpUtilSettings utilSettings = xmlMapper.readValue(is, XmlJpUtilSettings.class);
          if (utilSettings == null) {
            continue;
          }
          XmlJpUtils xmlJpUtils = utilSettings.getJpUtils();
          XmlJpUtil[] xmlJpUtilArr = xmlJpUtils != null ? xmlJpUtils.getJpUtil() : null;
          if (xmlJpUtilArr != null) {
            for (XmlJpUtil xmlJpUtil : xmlJpUtilArr) {
              if (xmlJpUtil.isDisable()) {
                continue;
              }
              result.add(toUtil(xmlJpUtil));
            }
          }
        }
      }
      return result;
    } catch (IOException e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  private Map<String, JPParam> toParams(XmlParams xml) {
    Map<String, JPParam> result = new LinkedHashMap<>(JPUtilSettings.DEFAULT_PARAMS);

    XmlParam[] xmlParamsArr = xml != null ? xml.getXmlParams() : null;
    if (xmlParamsArr != null) {
      for (XmlParam xmlParam : xmlParamsArr) {
        JPParam param = XmlParam.toParam(
            xmlParam,
            (x, jpType) -> parserService.parseTo(jpType.getJavaClass(), x.getValue())
        );
        if (param == null) {
          continue;
        }
        result.put(param.getCode(), param);
      }
    }
    return result;
  }

  private JPUtilSettings toUtil(XmlJpUtil xml) {
    String code = xml.getCode();
    String title = xml.getTitle();
    XmlJpViewRoles roles = xml.getRoles();
    XmlJpViewClasses jpClasses = xml.getJpClasses();
    XmlJpClassTags jpClassTags = xml.getJpClassTags();
    XmlJpUtilTags jpUtilTags = xml.getJpUtilTags();
    XmlJpDefValues jpDefValues = xml.getJpDefValues();
    XmlJpCheck jpCheck = xml.getJpCheck();
    XmlJpModes jpModes = xml.getJpModes();

    Collection<JPUtilModeSettings> modeList = new ArrayList<>();

    if (jpDefValues != null) {
      modeList.add(
          JPUtilModeSettingsBean.of(
              JPUtil.Mode.IN_PARAMS_DEF_VALUES, title + ". Значения по умолчанию", null,
              DefaultInParams.class,
              null, null, true,
              JPAppendType.CUSTOM, null, null, null,
              JPUtilSettings.DEFAULT_PARAMS.values(), false, false,
              null,
              JPUtilDefValuesOutParams.CODE, null,
              false
          )
      );
    }
    if (jpCheck != null) {
      modeList.add(
          JPUtilModeSettingsBean.of(
              JPUtil.Mode.CHECK_MODE, title + ". Проверка", null,
              DefaultInParams.class,
              null, null, true,
              JPAppendType.CUSTOM, null, null, null,
              JPUtilSettings.DEFAULT_PARAMS.values(), false, false,
              null,
              JPUtilCheckOutParams.CODE, null,
              false
          )
      );
    }
    Map<XmlJpMode, Collection<String>> modeMap = new HashMap<>();
    if (jpModes != null) {
      for (XmlJpMode jpMode : jpModes.getJpMode()) {
        Map<String, JPParam> modeParams = toParams(jpMode.getParams());
        XmlJpModeResult modeResult = jpMode.getJpResult();

        modeMap.put(jpMode, modeParams.keySet());
        modeList.add(
            JPUtilModeSettingsBean.of(
                jpMode.getCode(), jpMode.getTitle(), xml.getqName(),
                MapInParams.class,
                null, null, true,
                JPAppendType.getType(jpMode.getAppendType()),
                null, null, null,
                modeParams.values(), false, jpDefValues != null,
                null,
                modeResult.getType(), null,
                false
            )
        );
      }
    }

    return JPUtilSettingsBean.of(
        code, title, xml.getqName(),
        xml.getJpPackage(), roles != null ? roles.getRoles() : null,
        false, jpClasses != null ? jpClasses.getJpClasses() : null,
        jpClassTags != null ? jpClassTags.getTags() : null,
        jpUtilTags != null ? jpUtilTags.getTags() : null,
        JPUtilXmlAction.of(
            code, jpCheck, jpDefValues, modeMap,
            operations, transactionManager, shutdownManager, templateValueService
        ),
        modeList
    );
  }
}