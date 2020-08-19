package mp.jprime.dataaccess.params.query.filters;

import mp.jprime.dataaccess.enums.FilterOperation;

import java.time.LocalDate;

/**
 * Больше в месяцах
 */
public class GTMonth extends LocalDateDateFilter {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public GTMonth(String attrCode, LocalDate value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.GT_MONTH;
  }
}
