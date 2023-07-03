package mp.jprime.functions;

import mp.jprime.lang.JPMap;

/**
 * Сервис по работе с функциями
 */
public interface JPFunctionService {
  /**
   * Вычисление функции
   *
   * @param groupCode    Код группы
   * @param functionCode Код функции
   * @param params       Данные для вычисления
   * @return Значение
   */
  <T> T eval(String groupCode, String functionCode, Object... params);

  /**
   * Вычисление функции
   *
   * @param functionTemplate Вызов функции в типовом формате group.func$param1$param2
   * @param data             Данные
   * @param <T>              Тип значения
   * @return Значение
   */
  <T> T eval(String functionTemplate, JPMap data);

  /**
   * Вычисление функции
   *
   * @param functionTemplate Вызов функции в типовом формате group.func$param1$param2
   * @param <T>              Тип значения
   * @return Значение
   */
  default <T> T eval(String functionTemplate) {
    return eval(functionTemplate, null);
  }
}
