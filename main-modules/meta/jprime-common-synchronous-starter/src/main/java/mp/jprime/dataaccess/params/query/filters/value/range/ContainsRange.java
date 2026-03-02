package mp.jprime.dataaccess.params.query.filters.value.range;

import mp.jprime.dataaccess.enums.FilterOperation;

import mp.jprime.dataaccess.params.query.filters.value.CustomValueFilter;
import mp.jprime.lang.JPRange;

public class ContainsRange extends CustomValueFilter<JPRange<?>> {
  /**
   * Конструктор
   *
   * @param customValue Произвольное значение
   * @param value       Условие
   */
  public ContainsRange(Object customValue, JPRange<?> value) {
    super(customValue, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.CONTAINS_RANGE;
  }
}
