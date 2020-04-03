package mp.jprime.dataaccess.params.query.filters;

import mp.jprime.dataaccess.params.query.enums.FilterOperation;

import java.time.LocalDate;

/**
 * Меньше в месяцах
 */
public class LTMonth extends LocalDateDateFilter {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public LTMonth(String attrCode, LocalDate value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.LT_MONTH;
  }
}
