package mp.jprime.utils.loaders.xml.operations;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.checkers.JPDataCheckService;
import mp.jprime.dataaccess.checkers.JPDataCheckServiceAware;
import mp.jprime.dataaccess.templatevalues.JPTemplateValueService;
import mp.jprime.exceptions.JPAppRuntimeException;
import mp.jprime.security.AuthInfo;
import mp.jprime.utils.loaders.xml.JPUtilXmlOperation;
import mp.jprime.utils.loaders.xml.beans.XmlJpOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import java.util.Map;

/**
 * Проверка параметров
 */
@Service
public final class JPUtilXmlJpParamValidatorOperation extends JPUtilXmlBaseOperation
    implements JPDataCheckServiceAware {
  private JPDataCheckService checkService;
  private JPTemplateValueService templateValueService;

  @Override
  public void setJpDataCheckService(JPDataCheckService dataCheckService) {
    this.checkService = dataCheckService;
  }

  @Autowired
  private void setTemplateValueService(JPTemplateValueService templateValueService) {
    this.templateValueService = templateValueService;
  }

  @Override
  public String getCode() {
    return "jpParamValidator";
  }

  @Override
  public JPUtilXmlOperation.Executor newOperation(XmlJpOperation xml) {
    XmlSettings beans = getXmlMapper().toObject(XmlSettings.class, xml.getSettings());
    return new Executor(beans.errorMessage, beans.filter);
  }

  private class Executor extends JPUtilXmlBaseExecutor {
    private final String errorMessage;
    private final String filter;

    private Executor(String errorMessage, String filter) {
      this.errorMessage = errorMessage;
      this.filter = filter;
    }

    @Override
    public void execute(Map<String, JPObject> cache,
                        Map<String, Object> paramValues, ServerWebExchange swe, AuthInfo auth) {
      if (StringUtils.isBlank(filter)) {
        throw new JPAppRuntimeException(replaceParamValues(errorMessage, paramValues));
      }

      String sFilter = replaceParamValues(filter, paramValues);
      for (String pattern : templateValueService.getPatterns()) {
        if (sFilter.contains(pattern)) {
          Object value = templateValueService.getValue(pattern, auth);
          sFilter = StringUtils.replace(sFilter, pattern, value != null ? getParserService().toString(value) : "");
        }
      }
      if (!checkService.check(toFilter(sFilter), JPData.empty(), auth)) {
        throw new JPAppRuntimeException(replaceParamValues(errorMessage, paramValues));
      }
    }
  }

  private static class XmlSettings {
    @JacksonXmlProperty(localName = "errorMessage")
    private String errorMessage;
    @JacksonXmlProperty(localName = "filter")
    private String filter;
  }
}
