package mp.jprime.dataaccess.defvalues;

import mp.jprime.dataaccess.beans.JPMutableData;

/**
 * Логика вычисления значений по умолчанию при создании объекта
 */
public interface JPObjectDefValueService {
  /**
   * Возвращает значения по умолчанию
   *
   * @param jpClassCode Кодовое имя класса объекта для расчета значений по умолчанию
   * @param params      Параметры для вычисления значений по умолчанию
   */
  JPMutableData getDefValues(String jpClassCode, JPObjectDefValueParams params);
}