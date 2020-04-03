package mp.jprime.dataaccess.params.query.filters;

import mp.jprime.dataaccess.params.query.enums.FilterOperation;

/**
 * Больше или равно в годах
 */
public class GTEYear extends YearFilter {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public GTEYear(String attrCode, Integer value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.GTE_YEAR;
  }
}
