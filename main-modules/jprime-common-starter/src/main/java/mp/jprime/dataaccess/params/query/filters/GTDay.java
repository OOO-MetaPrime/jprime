package mp.jprime.dataaccess.params.query.filters;

import mp.jprime.dataaccess.params.query.enums.FilterOperation;

import java.time.LocalDate;

/**
 * Больше в днях
 */
public class GTDay extends LocalDateDateFilter {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public GTDay(String attrCode, LocalDate value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.GT_DAY;
  }
}
