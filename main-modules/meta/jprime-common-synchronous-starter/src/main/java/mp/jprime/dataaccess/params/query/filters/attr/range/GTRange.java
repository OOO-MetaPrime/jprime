package mp.jprime.dataaccess.params.query.filters.attr.range;

import mp.jprime.dataaccess.enums.FilterOperation;
import mp.jprime.dataaccess.params.query.filters.attr.AttrValueFilter;
import mp.jprime.lang.JPRange;

public class GTRange extends AttrValueFilter<JPRange<?>> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public GTRange(String attrCode, JPRange<?> value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.GT_RANGE;
  }

  @Override
  public GTRange ofAttr(String attrCode) {
    return new GTRange(attrCode, this.getValue());
  }
}
