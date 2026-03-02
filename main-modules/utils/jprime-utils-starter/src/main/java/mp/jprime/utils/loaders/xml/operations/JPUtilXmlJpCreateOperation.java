package mp.jprime.utils.loaders.xml.operations;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import mp.jprime.dataaccess.JPObjectRepositoryService;
import mp.jprime.dataaccess.JPObjectRepositoryServiceAware;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.security.AuthInfo;
import mp.jprime.utils.loaders.xml.JPUtilXmlOperation;
import mp.jprime.utils.loaders.xml.beans.XmlJpOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import java.util.HashMap;
import java.util.Map;

/**
 * Создание данных на основе меты
 */
@Service
public final class JPUtilXmlJpCreateOperation extends JPUtilXmlBaseOperation
    implements JPObjectRepositoryServiceAware {
  private JPObjectRepositoryService repo;

  @Override
  public void setJpObjectRepositoryService(JPObjectRepositoryService repo) {
    this.repo = repo;
  }

  @Override
  public String getCode() {
    return "jpCreate";
  }

  @Override
  public JPUtilXmlOperation.Executor newOperation(XmlJpOperation xml) {
    XmlSettings beans = getXmlMapper().toObject(XmlSettings.class, xml.getSettings());
    return new Executor(xml.getAlias(), xml.getForAlias(), beans.jpClass, beans.attrs);
  }

  private class Executor extends JPUtilXmlBaseExecutor {
    private final String alias;
    private final String forAlias;
    private final String jpClass;
    private final Map<String, String> attrs;

    private Executor(String alias, String forAlias, String jpClass, XmlAttrs xmlAttrs) {
      this.alias = alias;
      this.forAlias = forAlias;

      this.jpClass = jpClass;
      Map<String, String> attrs = null;
      if (xmlAttrs != null) {
        attrs = new HashMap<>();
        for (XmlAttr xmlAttr : xmlAttrs.attr) {
          attrs.put(xmlAttr.code, xmlAttr.value);
        }
      }
      this.attrs = attrs;
    }

    @Override
    public void execute(Map<String, JPObject> cache,
                        Map<String, Object> paramValues, ServerWebExchange swe, AuthInfo auth) {
      JPObject object;
      if (StringUtils.isBlank(jpClass)) {
        object = null;
      } else {
        JPMutableData attrValues = JPMutableData.empty();
        for (var entry : attrs.entrySet()) {
          attrValues.put(entry.getKey(), replaceParamValues(entry.getValue(), paramValues));
        }

        object = repo.createAndGet(
            JPCreate.create(jpClass)
                .set(attrValues)
                .auth(auth)
                .build()
        );
      }
      cache.put(alias, object);
    }
  }

  private static class XmlSettings {
    @JacksonXmlProperty(localName = "jpClass")
    private String jpClass;
    @JacksonXmlProperty(localName = "attrs")
    private XmlAttrs attrs;
  }

  private static class XmlAttrs {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "attr")
    private XmlAttr[] attr;
  }


  private static class XmlAttr {
    @JacksonXmlProperty(localName = "code")
    private String code;
    @JacksonXmlProperty(localName = "value")
    private String value;
  }
}
