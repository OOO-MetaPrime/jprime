package mp.jprime.dataaccess.params.query.filters.attr;

import mp.jprime.dataaccess.enums.FilterOperation;

/**
 * Значение указано
 */
public class NotNull extends AttrValueFilter<Comparable> {
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

  @Override
  public NotNull ofAttr(String attrCode) {
    return new NotNull(attrCode);
  }
}
