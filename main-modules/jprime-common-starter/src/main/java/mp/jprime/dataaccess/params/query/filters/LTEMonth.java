package mp.jprime.dataaccess.params.query.filters;

import mp.jprime.dataaccess.enums.FilterOperation;

import java.time.LocalDate;

/**
 * Меньше или равно в месяцах
 */
public class LTEMonth extends LocalDateDateFilter {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public LTEMonth(String attrCode, LocalDate value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.LTE_MONTH;
  }
}
