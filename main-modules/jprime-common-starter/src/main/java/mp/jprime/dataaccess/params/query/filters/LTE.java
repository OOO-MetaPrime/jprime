package mp.jprime.dataaccess.params.query.filters;

import mp.jprime.dataaccess.params.query.enums.FilterOperation;

/**
 * Меньше или равно
 */
public class LTE extends ValueFilter<Object> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public LTE(String attrCode, Object value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.LTE;
  }
}
