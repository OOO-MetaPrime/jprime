package mp.jprime.utils.loaders.xml.operations;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import mp.jprime.dataaccess.JPObjectRepositoryService;
import mp.jprime.dataaccess.JPObjectRepositoryServiceAware;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.params.JPSelect;
import mp.jprime.security.AuthInfo;
import mp.jprime.utils.loaders.xml.JPUtilXmlOperation;
import mp.jprime.utils.loaders.xml.beans.XmlJpOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

/**
 * Получение данных на основе меты
 */
@Service
public final class JPUtilXmlJpSelectOperation extends JPUtilXmlBaseOperation
    implements JPObjectRepositoryServiceAware {
  private JPObjectRepositoryService repo;

  @Override
  public void setJpObjectRepositoryService(JPObjectRepositoryService repo) {
    this.repo = repo;
  }

  @Override
  public String getCode() {
    return "jpSelect";
  }

  @Override
  public JPUtilXmlOperation.Executor newOperation(XmlJpOperation xml) {
    XmlSettings beans = getXmlMapper().toObject(XmlSettings.class, xml.getSettings());
    return new Executor(xml.getAlias(), xml.getForAlias(), beans.jpClass, beans.attrs, beans.filter);
  }

  private class Executor extends JPUtilXmlBaseExecutor {
    private final String alias;
    private final String forAlias;
    private final String jpClass;
    private final Collection<String> attrs;
    private final String filter;

    private Executor(String alias, String forAlias, String jpClass, XmlAttrs xmlAttrs, String filter) {
      this.alias = alias;
      this.forAlias = forAlias;

      this.jpClass = jpClass;
      Collection<String> attrs = null;
      if (xmlAttrs != null) {
        attrs = new HashSet<>();
        for (XmlAttr xmlAttr : xmlAttrs.attr) {
          attrs.add(xmlAttr.code);
        }
      }
      this.attrs = attrs;
      this.filter = filter;
    }

    @Override
    public void execute(Map<String, JPObject> cache,
                        Map<String, Object> paramValues, ServerWebExchange swe, AuthInfo auth) {
      JPObject object;
      if (StringUtils.isBlank(jpClass) || StringUtils.isBlank(filter)) {
        object = null;
      } else {
        JPObject forObject = forAlias != null ? cache.get(forAlias) : null;

        String sFilter = replaceParamValues(filter, paramValues);

        sFilter = forObject == null ? sFilter : replaceAttrValues(sFilter, forObject);

        object = repo.getObject(
            JPSelect.from(jpClass)
                .attrs(attrs)
                .where(toFilter(sFilter))
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
    @JacksonXmlProperty(localName = "filter")
    private String filter;
  }

  private static class XmlAttrs {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "attr")
    private XmlAttr[] attr;
  }


  private static class XmlAttr {
    @JacksonXmlProperty(localName = "code")
    private String code;
  }
}
