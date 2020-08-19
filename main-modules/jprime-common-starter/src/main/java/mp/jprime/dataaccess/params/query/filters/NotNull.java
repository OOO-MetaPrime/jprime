package mp.jprime.dataaccess.params.query.filters;

import mp.jprime.dataaccess.enums.FilterOperation;

/**
 * Значение указано
 */
public class NotNull extends ValueFilter<Comparable> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   */
  public NotNull(String attrCode) {
    super(attrCode, null);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.ISNOTNULL;
  }
}
