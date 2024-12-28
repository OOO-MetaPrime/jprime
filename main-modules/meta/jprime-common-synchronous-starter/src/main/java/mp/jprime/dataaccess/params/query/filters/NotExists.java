package mp.jprime.dataaccess.params.query.filters;

import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.dataaccess.enums.AnalyticFunction;

/**
 * Не существует
 */
public class NotExists extends LinkFilter {
  /**
   * Конструктор
   *
   * @param attrCode Кодовое имя атрибута
   * @param filter   Описание условия
   */
  public NotExists(String attrCode, Filter filter) {
    super(attrCode, filter);
  }

  /**
   * Условие
   *
   * @return Условие
   */
  @Override
  public AnalyticFunction getFunction() {
    return AnalyticFunction.NOTEXISTS;
  }
}
