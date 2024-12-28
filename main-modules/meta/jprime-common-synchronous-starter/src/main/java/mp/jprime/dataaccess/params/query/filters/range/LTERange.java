package mp.jprime.dataaccess.params.query.filters.range;

import mp.jprime.dataaccess.enums.FilterOperation;
import mp.jprime.dataaccess.params.query.filters.ValueFilter;
import mp.jprime.lang.JPRange;

public class LTERange extends ValueFilter<JPRange<?>> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public LTERange(String attrCode, JPRange<?> value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.LTE_RANGE;
  }
}
