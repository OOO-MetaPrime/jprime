package mp.jprime.dataaccess.params.query.filters;

import mp.jprime.dataaccess.enums.FilterOperation;

import java.time.LocalDate;

/**
 * Меньше или равно в днях
 */
public class LTEDay extends LocalDateDateFilter {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public LTEDay(String attrCode, LocalDate value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.LTE_DAY;
  }
}
