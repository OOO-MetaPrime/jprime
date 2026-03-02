package mp.jprime.dataaccess.checkers.filters;

import mp.jprime.dataaccess.checkers.JPDataCheckService;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.dataaccess.templatevalues.JPTemplateValue;
import mp.jprime.dataaccess.templatevalues.JPTemplateValueService;
import mp.jprime.lang.JPArray;
import mp.jprime.parsers.ParserService;
import mp.jprime.security.AuthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

public abstract class CheckBaseFilter<T extends Filter> implements CheckFilter<T> {
  @Service
  private static final class Links {
    private static JPDataCheckService DATA_CHECK_SERVICE;
    private static ParserService PARSER_SERVICE;
    private static JPTemplateValueService TEMPLATE_VALUE_SERVICE;

    private Links(@Autowired JPDataCheckService jpDataCheckService,
                  @Autowired ParserService parserService,
                  @Autowired JPTemplateValueService jpTemplateValueService) {
      DATA_CHECK_SERVICE = jpDataCheckService;
      PARSER_SERVICE = parserService;
      TEMPLATE_VALUE_SERVICE = jpTemplateValueService;
    }
  }

  protected JPDataCheckService getJpDataCheckService() {
    return Links.DATA_CHECK_SERVICE;
  }

  protected ParserService getParserService() {
    return Links.PARSER_SERVICE;
  }

  protected JPTemplateValueService getTemplateValueService() {
    return Links.TEMPLATE_VALUE_SERVICE;
  }

  protected <T> T parseTo(Class<T> toClass, Object filterValue, AuthInfo auth) {
    if (filterValue == null) {
      return null;
    }
    filterValue = getTemplateValueService().getValue(filterValue, auth);
    if (filterValue == null) {
      return null;
    }
    return getParserService().parseTo(toClass, filterValue);
  }

  protected Class getValueClass(Object value) {
    if (value instanceof Collection x) {
      if (x.isEmpty()) {
        return null;
      }
      return x.iterator().next().getClass();
    }
    if (value instanceof JPArray x) {
      Collection list = x.toList();
      if (list.isEmpty()) {
        return null;
      }
      return list.iterator().next().getClass();
    }
    return value.getClass();
  }

  protected record ParsedCollection(Collection<? extends Comparable> values, boolean emptyValuesIgnore) {

  }

  protected ParsedCollection parseToCollection(Class toClass, Collection<? extends Comparable> fromValues, AuthInfo auth) {
    if (fromValues == null) {
      return null;
    }

    Collection values = new ArrayList<>();
    boolean emptyValueIgnore = false;
    for (Comparable fromValue : fromValues) {
      JPTemplateValue template = getTemplateValueService().getTemplate(fromValue);
      emptyValueIgnore = template != null && template.isEmptyValueIgnore();
      addValue(values, toClass, getTemplateValueService().getValue(template, fromValue, auth), auth);
    }
    return new ParsedCollection(values, emptyValueIgnore);
  }

  private void addValue(Collection values, Class toClass, Object value, AuthInfo auth) {
    if (value instanceof Iterable<?> i) {
      i.forEach(x -> addValue(values, toClass, x, auth));
    } else {
      Object newV = parseTo(toClass, value, auth);
      if (newV == null) {
        return;
      }
      if (newV instanceof JPArray list) {
        values.addAll(list.toList());
      } else {
        values.add(newV);
      }
    }
  }
}
