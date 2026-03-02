package mp.jprime.dataaccess.params.query.filters.value;

import mp.jprime.dataaccess.enums.FilterOperation;
import mp.jprime.dataaccess.params.query.data.KeyValuePair;

/**
 * Содержит пару ключ-значение
 */
public class ContainsKVP extends CustomValueFilter<KeyValuePair> {
  /**
   * Конструктор
   *
   * @param customValue Произвольное значение
   * @param value       Ключ-значение
   */
  public ContainsKVP(Object customValue, KeyValuePair value) {
    super(customValue, value);
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
