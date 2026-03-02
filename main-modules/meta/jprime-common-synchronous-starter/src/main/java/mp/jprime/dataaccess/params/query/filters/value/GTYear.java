package mp.jprime.dataaccess.params.query.filters.value;

import mp.jprime.dataaccess.enums.FilterOperation;

/**
 * Больше в годах
 */
public class GTYear extends YearFilter {
  /**
   * Конструктор
   *
   * @param customValue Произвольное значение
   * @param value       Условие
   */
  public GTYear(Object customValue, Integer value) {
    super(customValue, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.GT_YEAR;
  }
}
