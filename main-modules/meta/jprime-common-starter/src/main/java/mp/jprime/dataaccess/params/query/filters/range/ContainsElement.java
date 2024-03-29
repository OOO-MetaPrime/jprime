package mp.jprime.dataaccess.params.query.filters.range;

import mp.jprime.dataaccess.enums.FilterOperation;
import mp.jprime.dataaccess.params.query.filters.ValueFilter;

public class ContainsElement extends ValueFilter<Object> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public ContainsElement(String attrCode, Object value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.CONTAINS_ELEMENT;
  }
}
