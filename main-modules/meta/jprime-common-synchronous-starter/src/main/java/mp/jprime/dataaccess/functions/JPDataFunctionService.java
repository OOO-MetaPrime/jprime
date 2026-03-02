package mp.jprime.dataaccess.functions;

import mp.jprime.lang.JPMutableMap;
import mp.jprime.security.AuthInfo;

import java.util.List;
import java.util.Map;

/**
 * Работа с функциями формирования данных, доступными в системе
 */
public interface JPDataFunctionService {
  /**
   * Вызывает все доступные функции с переданными параметрами
   *
   * @param data данные
   */
  default void eval(JPMutableMap data) {
    eval(data, null);
  }

  /**
   * Вызывает все доступные функции с переданными параметрами
   *
   * @param data данные
   * @param auth AuthInfo
   */
  default void eval(JPMutableMap data, AuthInfo auth) {
    if (data == null || data.isEmpty()) {
      return;
    }
    eval(List.of(data), auth);
  }

  /**
   * Вызывает все доступные функции с переданными параметрами
   *
   * @param data данные
   * @param auth AuthInfo
   */
  default void eval(List<? extends JPMutableMap> data, AuthInfo auth) {
    if (data == null || data.isEmpty()) {
      return;
    }
    eval(Map.of("single", data), auth);
  }

  /**
   * Вызывает все доступные функции с переданными параметрами
   *
   * @param data данные
   * @param auth AuthInfo
   */
  default void eval(Map<String, List<? extends JPMutableMap>> data, AuthInfo auth) {
    eval(data, null, auth);
  }

  /**
   * Вызывает все доступные функции с переданными параметрами
   *
   * @param data           данные
   * @param formatConsumer Обработчик формата поля
   * @param auth           AuthInfo
   */
  void eval(Map<String, List<? extends JPMutableMap>> data, FormatConsumer formatConsumer, AuthInfo auth);

  @FunctionalInterface
  interface FormatConsumer {
    void accept(String sourceCode, String fieldCode, String format);
  }
}
