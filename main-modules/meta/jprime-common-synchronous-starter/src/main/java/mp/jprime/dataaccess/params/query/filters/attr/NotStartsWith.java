package mp.jprime.dataaccess.params.query.filters.attr;

import mp.jprime.dataaccess.enums.FilterOperation;

/**
 * Не начинается С
 */
public class NotStartsWith extends AttrValueFilter<String> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public NotStartsWith(String attrCode, String value) {
    super(attrCode, value);
  }

  @Override
  public FilterOperation getOper() {
    return FilterOperation.NOT_STARTS_WITH;
  }

  @Override
  public NotStartsWith ofAttr(String attrCode) {
    return new NotStartsWith(attrCode, this.getValue());
  }
}

