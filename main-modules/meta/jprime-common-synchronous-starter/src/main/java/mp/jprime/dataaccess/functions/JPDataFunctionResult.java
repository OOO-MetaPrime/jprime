package mp.jprime.dataaccess.functions;

/**
 * Результат работы функции
 *
 * @param <V> Тип значения
 */
public interface JPDataFunctionResult<V> {
  /**
   * Результат вычисления
   *
   * @return Результат вычисления
   */
  V getResult();

  /**
   * Формат результата
   *
   * @return Формат результата
   */
  String getFormat();
}
