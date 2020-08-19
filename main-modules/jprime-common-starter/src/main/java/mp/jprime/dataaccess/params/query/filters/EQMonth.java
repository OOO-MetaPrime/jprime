package mp.jprime.dataaccess.params.query.filters;

import mp.jprime.dataaccess.enums.FilterOperation;

import java.time.LocalDate;

/**
 * Равно в месяцах
 */
public class EQMonth extends LocalDateDateFilter {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public EQMonth(String attrCode, LocalDate value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.EQ_MONTH;
  }
}
