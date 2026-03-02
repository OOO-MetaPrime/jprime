package mp.jprime.dataaccess.params.query.filters.attr;

import mp.jprime.dataaccess.enums.FilterOperation;
import mp.jprime.dataaccess.params.query.data.KeyValuePair;

/**
 * Содержит пару ключ-значение
 */
public class ContainsKVP extends AttrValueFilter<KeyValuePair> {
  /**
   * Конструктор
   *
   * @param attrCode Условие по значениям
   * @param value    Ключ-значение
   */
  public ContainsKVP(String attrCode, KeyValuePair value) {
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

  @Override
  public ContainsKVP ofAttr(String attrCode) {
    return new ContainsKVP(attrCode, this.getValue());
  }
}
