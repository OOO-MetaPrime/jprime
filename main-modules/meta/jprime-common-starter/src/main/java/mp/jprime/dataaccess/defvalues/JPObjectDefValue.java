package mp.jprime.dataaccess.defvalues;

import mp.jprime.dataaccess.beans.JPMutableData;

/**
 * Значения по умолчанию
 */
public interface JPObjectDefValue {
  /**
   * Дополняет значения по умолчанию
   *
   * @param jpData Значение по умолчанию
   * @param params Параметры для вычисления значений по умолчанию
   */
  void appendValues(JPMutableData jpData, JPObjectDefValueParams params);
}