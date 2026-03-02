package mp.jprime.dataaccess.params.query.filters.attr;

import mp.jprime.dataaccess.enums.FilterOperation;

/**
 * Начинается С
 */
public class StartsWith extends AttrValueFilter<Object> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public StartsWith(String attrCode, Object value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.STARTS_WITH;
  }

  @Override
  public StartsWith ofAttr(String attrCode) {
    return new StartsWith(attrCode, this.getValue());
  }
}

