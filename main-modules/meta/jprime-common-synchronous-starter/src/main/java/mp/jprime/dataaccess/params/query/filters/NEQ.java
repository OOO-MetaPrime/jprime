package mp.jprime.dataaccess.params.query.filters;

import mp.jprime.dataaccess.enums.FilterOperation;

/**
 * Не равно
 */
public class NEQ extends ValueFilter<Object> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public NEQ(String attrCode, Object value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.NEQ;
  }
}
