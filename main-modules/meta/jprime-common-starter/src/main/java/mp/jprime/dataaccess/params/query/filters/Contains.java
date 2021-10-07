package mp.jprime.dataaccess.params.query.filters;

import mp.jprime.dataaccess.enums.FilterOperation;
import mp.jprime.dataaccess.params.query.data.Entry;

/**
 * Содержит
 */
public class Contains extends ValueFilter<Entry> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Ключ-значение
   */
  public Contains(String attrCode, Entry value) {
    super(attrCode, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.CONTAINS;
  }
}
