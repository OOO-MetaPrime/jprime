package mp.jprime.dataaccess.params.query.filters.attr.range;

import mp.jprime.dataaccess.enums.FilterOperation;
import mp.jprime.dataaccess.params.query.filters.attr.AttrValueFilter;
import mp.jprime.lang.JPRange;

public class EQRange extends AttrValueFilter<JPRange<?>> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public EQRange(String attrCode, JPRange<?> value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.EQ_RANGE;
  }

  @Override
  public EQRange ofAttr(String attrCode) {
    return new EQRange(attrCode, this.getValue());
  }
}
