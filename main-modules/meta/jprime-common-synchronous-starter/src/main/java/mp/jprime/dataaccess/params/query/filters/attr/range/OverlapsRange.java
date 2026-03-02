package mp.jprime.dataaccess.params.query.filters.attr.range;

import mp.jprime.dataaccess.enums.FilterOperation;
import mp.jprime.dataaccess.params.query.filters.attr.AttrValueFilter;
import mp.jprime.lang.JPRange;

/**
 * Диапазон пересекается c диапазоном
 */
public class OverlapsRange extends AttrValueFilter<JPRange<?>> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public OverlapsRange(String attrCode, JPRange<?> value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.OVERLAPS_RANGE;
  }

  @Override
  public OverlapsRange ofAttr(String attrCode) {
    return new OverlapsRange(attrCode, this.getValue());
  }
}
