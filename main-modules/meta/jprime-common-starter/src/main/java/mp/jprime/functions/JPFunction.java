package mp.jprime.functions;

/**
 * Функция
 */
public interface JPFunction<T> {
  /**
   * Вычисление функции
   *
   * @param params Данные для вычисления
   * @return Значение
   */
  T eval(Object... params);
}
