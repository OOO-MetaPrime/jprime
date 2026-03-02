package mp.jprime.dataaccess.params.query.filters.attr.array;

import mp.jprime.dataaccess.enums.FilterOperation;
import mp.jprime.dataaccess.params.query.filters.attr.AttrValueFilter;
import mp.jprime.lang.JPArray;

/**
 * Массив пересекается с массивом
 */
public class OverlapsArray extends AttrValueFilter<JPArray<?>> {
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

  @Override
  public OverlapsArray ofAttr(String attrCode) {
    return new OverlapsArray(attrCode, this.getValue());
  }
}
