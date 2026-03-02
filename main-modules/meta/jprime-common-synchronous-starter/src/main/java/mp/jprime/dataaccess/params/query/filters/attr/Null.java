package mp.jprime.dataaccess.params.query.filters.attr;

import mp.jprime.dataaccess.enums.FilterOperation;

/**
 * Значение не указано
 */
public class Null extends AttrValueFilter<Comparable> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   */
  public Null(String attrCode) {
    super(attrCode, null);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.ISNULL;
  }

  @Override
  public Null ofAttr(String attrCode) {
    return new Null(attrCode);
  }
}
