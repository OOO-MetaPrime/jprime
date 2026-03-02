package mp.jprime.dataaccess.params.query.filters.value;

import mp.jprime.dataaccess.enums.FilterOperation;

/**
 * Меньше или равно в годах
 */
public class LTEYear extends YearFilter {
  /**
   * Конструктор
   *
   * @param customValue Произвольное значение
   * @param value       Условие
   */
  public LTEYear(Object customValue, Integer value) {
    super(customValue, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.LTE_YEAR;
  }
}
