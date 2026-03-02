package mp.jprime.dataaccess.params.query.filters.value.array;

import mp.jprime.dataaccess.enums.FilterOperation;

import mp.jprime.dataaccess.params.query.filters.value.CustomValueFilter;
import mp.jprime.lang.JPArray;

/**
 * Массив пересекается с массивом
 */
public class OverlapsArray extends CustomValueFilter<JPArray<?>> {
  /**
   * Конструктор
   *
   * @param customValue Произвольное значение
   * @param value       Условие
   */
  public OverlapsArray(Object customValue, JPArray<?> value) {
    super(customValue, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.OVERLAPS_ARRAY;
  }
}
