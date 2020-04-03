package mp.jprime.dataaccess.params.query.filters;

import mp.jprime.dataaccess.params.query.enums.FilterOperation;

/**
 * Содержит
 */
public class Like extends ValueFilter<Object> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public Like(String attrCode, Object value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.LIKE;
  }
}
