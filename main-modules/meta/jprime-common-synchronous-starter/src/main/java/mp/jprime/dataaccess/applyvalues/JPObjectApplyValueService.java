package mp.jprime.dataaccess.applyvalues;

import mp.jprime.dataaccess.beans.JPData;

/**
 * Логика для дополнения значений.
 * В ответе будут только данные, подлежащие изменению, входящая data в ответе не дублируется.
 */
public interface JPObjectApplyValueService {
  /**
   * Возвращает дополненные значения
   *
   * @param params Параметры для дополнения значений
   */
  JPData getApplyValues(JPObjectApplyValueParams params);
}