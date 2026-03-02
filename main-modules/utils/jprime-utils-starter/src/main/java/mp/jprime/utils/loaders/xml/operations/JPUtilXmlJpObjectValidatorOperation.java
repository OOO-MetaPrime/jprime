package mp.jprime.utils.loaders.xml.operations;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.checkers.JPDataCheckService;
import mp.jprime.dataaccess.checkers.JPDataCheckServiceAware;
import mp.jprime.exceptions.JPAppRuntimeException;
import mp.jprime.security.AuthInfo;
import mp.jprime.utils.loaders.xml.JPUtilXmlOperation;
import mp.jprime.utils.loaders.xml.beans.XmlJpOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import java.util.Map;

/**
 * Проверка данных на основе jpObject
 */
@Service
public final class JPUtilXmlJpObjectValidatorOperation extends JPUtilXmlBaseOperation
    implements JPDataCheckServiceAware {
  private JPDataCheckService checkService;

  @Override
  public void setJpDataCheckService(JPDataCheckService dataCheckService) {
    this.checkService = dataCheckService;
  }

  @Override
  public String getCode() {
    return "jpObjectValidator";
  }

  @Override
  public JPUtilXmlOperation.Executor newOperation(XmlJpOperation xml) {
    XmlSettings beans = getXmlMapper().toObject(XmlSettings.class, xml.getSettings());
    return new Executor(xml.getForAlias(), beans.errorMessage, beans.empty, beans.filter);
  }

  private class Executor extends JPUtilXmlBaseExecutor {
    private final String forAlias;
    private final String errorMessage;
    private final boolean empty;
    private final String filter;

    private Executor(String forAlias, String errorMessage, boolean empty, String filter) {
      this.forAlias = forAlias;
      this.errorMessage = errorMessage;
      this.empty = empty;
      this.filter = filter;
    }

    @Override
    public void execute(Map<String, JPObject> cache,
                        Map<String, Object> paramValues, ServerWebExchange swe, AuthInfo auth) {
      JPObject object = cache.get(forAlias);

      if (empty) {
        if (object == null) {
          return;
        } else {
          throw new JPAppRuntimeException(replaceParamValues(errorMessage, paramValues));
        }
      }

      if (StringUtils.isBlank(filter)) {
        throw new JPAppRuntimeException(replaceParamValues(errorMessage, paramValues));
      }

      String sFilter = replaceParamValues(filter, paramValues);
      if (!checkService.check(toFilter(sFilter), object != null ? object.getData() : JPData.empty(), auth)) {
        throw new JPAppRuntimeException(replaceParamValues(errorMessage, paramValues));
      }
    }
  }

  private static class XmlSettings {
    @JacksonXmlProperty(localName = "errorMessage")
    private String errorMessage;
    @JacksonXmlProperty(localName = "empty")
    private boolean empty;
    @JacksonXmlProperty(localName = "filter")
    private String filter;
  }
}
