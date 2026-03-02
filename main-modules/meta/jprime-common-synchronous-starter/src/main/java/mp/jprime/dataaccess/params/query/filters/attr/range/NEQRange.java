package mp.jprime.dataaccess.params.query.filters.attr.range;

import mp.jprime.dataaccess.enums.FilterOperation;
import mp.jprime.dataaccess.params.query.filters.attr.AttrValueFilter;
import mp.jprime.lang.JPRange;

public class NEQRange extends AttrValueFilter<JPRange<?>> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public NEQRange(String attrCode, JPRange<?> value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.NEQ_RANGE;
  }

  @Override
  public NEQRange ofAttr(String attrCode) {
    return new NEQRange(attrCode, this.getValue());
  }
}
