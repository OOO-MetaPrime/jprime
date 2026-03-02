package mp.jprime.dataaccess.params.query.filters.attr;

import mp.jprime.dataaccess.enums.FilterOperation;

/**
 * Не равно в годах
 */
public class NEQYear extends YearFilter {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public NEQYear(String attrCode, Integer value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.NEQ_YEAR;
  }

  @Override
  public NEQYear ofAttr(String attrCode) {
    return new NEQYear(attrCode, this.getValue());
  }
}
