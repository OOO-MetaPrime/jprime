package mp.jprime.dataaccess.params.query.filters;

import mp.jprime.dataaccess.enums.FilterOperation;

/**
 * Нечеткий поиск с учетом порядка лексем
 */
public class FuzzyOrderLike extends ValueFilter<String> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public FuzzyOrderLike(String attrCode, String value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.FUZZY_ORDER_LIKE;
  }
}
