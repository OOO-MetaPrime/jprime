package mp.jprime.parsers.stringformat.exceptions;

import mp.jprime.exceptions.JPAppRuntimeException;

import java.util.Collections;
import java.util.Map;

/**
 * Ошибка форматирования данных
 */
public class JPStringFormatParseException extends JPAppRuntimeException {
  private final Map<String, String> values;

  /**
   * Конструктор
   *
   * @param values Поля с ошибками
   */
  public JPStringFormatParseException(Map<String, String> values, Map<String, String> titles) {
    super("stringFormat.error", "Неверно указано значение: " + String.join(",", titles.values()));
    this.values = Collections.unmodifiableMap(values);
  }

  /**
   * Конструктор
   *
   * @param values Поля с ошибками
   */
  public JPStringFormatParseException(Map<String, String> values) {
    super("stringFormat.error", "Неверно указано значение: " + String.join(",", values.keySet()));
    this.values = Collections.unmodifiableMap(values);
  }

  public Map<String, String> getValues() {
    return values;
  }
}
