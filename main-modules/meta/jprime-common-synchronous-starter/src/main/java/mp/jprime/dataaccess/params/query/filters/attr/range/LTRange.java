package mp.jprime.dataaccess.params.query.filters.attr.range;

import mp.jprime.dataaccess.enums.FilterOperation;
import mp.jprime.dataaccess.params.query.filters.attr.AttrValueFilter;
import mp.jprime.lang.JPRange;

public class LTRange extends AttrValueFilter<JPRange<?>> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public LTRange(String attrCode, JPRange<?> value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.LT_RANGE;
  }

  @Override
  public LTRange ofAttr(String attrCode) {
    return new LTRange(attrCode, this.getValue());
  }
}
