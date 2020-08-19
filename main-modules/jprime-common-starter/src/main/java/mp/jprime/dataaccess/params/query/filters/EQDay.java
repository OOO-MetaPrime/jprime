package mp.jprime.dataaccess.params.query.filters;

import mp.jprime.dataaccess.enums.FilterOperation;

import java.time.LocalDate;

/**
 * Равно в днях
 */
public class EQDay extends LocalDateDateFilter {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public EQDay(String attrCode, LocalDate value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.EQ_DAY;
  }
}
