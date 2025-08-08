package mp.jprime.dataaccess.params.query.filters.array;

import mp.jprime.dataaccess.enums.FilterOperation;
import mp.jprime.dataaccess.params.query.filters.ValueFilter;
import mp.jprime.lang.JPArray;

/**
 * Массив пересекается с массивом
 */
public class OverlapsArray extends ValueFilter<JPArray<?>> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public OverlapsArray(String attrCode, JPArray<?> value) {
    super(attrCode, value);
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
