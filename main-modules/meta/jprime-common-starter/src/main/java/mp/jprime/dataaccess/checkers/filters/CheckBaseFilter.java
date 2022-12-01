package mp.jprime.dataaccess.checkers.filters;

import mp.jprime.dataaccess.checkers.JPDataCheckService;
import mp.jprime.dataaccess.checkers.JPDataCheckServiceAware;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.dataaccess.templatevalues.JPTemplateValueService;
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

  protected <T> T parseTo(Class<T> toClass, Object filterValue, AuthInfo authInfo) {
    if (filterValue == null) {
      return null;
    }
    filterValue = jpTemplateValueService.getValue(filterValue, authInfo);
    if (filterValue == null) {
      return null;
    }
    return getParserService().parseTo(toClass, filterValue);
  }

  protected <T> Collection<T> parseToCollection(Class<T> toClass, Collection<? extends Comparable> filterValues, AuthInfo authInfo) {
    Collection<T> values = null;
    if (filterValues != null) {
      values = new ArrayList<>(filterValues.size());
      for (Comparable v : filterValues) {
        T newV = parseTo(toClass, v, authInfo);
        if (newV == null) {
          continue;
        }
        values.add(newV);
      }
    }
    return values;
  }
}
