package mp.jprime.dataaccess.checkers.filters;

import mp.jprime.dataaccess.checkers.JPDataCheckService;
import mp.jprime.dataaccess.checkers.JPDataCheckServiceAware;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.dataaccess.templatevalues.JPTemplateValue;
import mp.jprime.dataaccess.templatevalues.JPTemplateValueService;
import mp.jprime.lang.JPArray;
import mp.jprime.parsers.ParserService;
import mp.jprime.parsers.ParserServiceAware;
import mp.jprime.security.AuthInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;

public abstract class CheckBaseFilter<T extends Filter> implements CheckFilter<T>, JPDataCheckServiceAware, ParserServiceAware {
  // Сервис проверки данных указанному условию
  private JPDataCheckService jpDataCheckService;
  // Парсер типов
  private ParserService parserService;
  // Сервис получения шаблонных значения
  private JPTemplateValueService jpTemplateValueService;

  @Override
  public void setJpDataCheckService(JPDataCheckService jpDataCheckService) {
    this.jpDataCheckService = jpDataCheckService;
  }

  @Override
  public void setParserService(ParserService parserService) {
    this.parserService = parserService;
  }

  @Autowired
  private void setJpTemplateValueService(JPTemplateValueService jpTemplateValueService) {
    this.jpTemplateValueService = jpTemplateValueService;
  }

  protected JPDataCheckService getJpDataCheckService() {
    return jpDataCheckService;
  }

  protected ParserService getParserService() {
    return parserService;
  }

  protected <T> T parseTo(Class<T> toClass, Object filterValue, AuthInfo auth) {
    if (filterValue == null) {
      return null;
    }
    filterValue = jpTemplateValueService.getValue(filterValue, auth);
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
      JPTemplateValue template = jpTemplateValueService.getTemplate(fromValue);
      emptyValueIgnore = template != null && template.isEmptyValueIgnore();
      addValue(values, toClass, jpTemplateValueService.getValue(template, fromValue, auth), auth);
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
