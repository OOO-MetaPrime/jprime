package mp.jprime.dataaccess.params.query.filters;

import mp.jprime.dataaccess.enums.FilterOperation;

/**
 * Больше
 */
public class GT extends ValueFilter<Object> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public GT(String attrCode, Object value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.GT;
  }
}
