package mp.jprime.dataaccess.params.query.filters;

import mp.jprime.dataaccess.enums.FilterOperation;

/**
 * Не начинается С
 */
public class NotStartsWith extends ValueFilter<Object> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Условие
   */
  public NotStartsWith(String attrCode, Object value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.NOT_STARTS_WITH;
  }
}

