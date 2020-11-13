package mp.jprime.dataaccess.conds;

import mp.jprime.dataaccess.enums.FilterOperation;

/**
 * Условие
 */
public interface ValueCond<T> {
  /**
   * Операция
   *
   * @return Операция
   */
  FilterOperation getOper();

  /**
   * Значение
   *
   * @return Значение
   */
  T getValue();
}
