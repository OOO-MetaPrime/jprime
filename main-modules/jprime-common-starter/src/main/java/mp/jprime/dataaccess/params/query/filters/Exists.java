package mp.jprime.dataaccess.params.query.filters;

import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.dataaccess.params.query.enums.AnalyticFunction;

/**
 * Существует
 */
public class Exists extends LinkFilter {
  /**
   * Конструктор
   *
   * @param attrCode Кодовое имя атрибута
   * @param filter   Описание условия
   */
  public Exists(String attrCode, Filter filter) {
    super(attrCode, filter);
  }

  /**
   * Условие
   *
   * @return Условие
   */
  @Override
  public AnalyticFunction getFunction() {
    return AnalyticFunction.EXISTS;
  }
}