package mp.jprime.dataaccess.applyvalues;

import mp.jprime.dataaccess.beans.JPMutableData;

/**
 * Логика по дополнению значений
 */
public interface JPObjectApplyValue {
  /**
   * Дополняет или модифицирует значения
   * Если данные не подлежат изменениям, то передавать исходные jpData в результат нет необходимости.
   *
   * @param params Параметры для дополнения значений
   */
  JPMutableData getAppendValues(JPObjectApplyValueParams params);
}