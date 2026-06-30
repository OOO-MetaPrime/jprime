package mp.jprime.parsers.doctypeformat.exceptions;

import mp.jprime.exceptions.JPAppRuntimeException;

import java.util.Collections;
import java.util.Map;

/**
 * Ошибка форматирования данных
 */
public class JpDocTypeFormatParseException extends JPAppRuntimeException {
  private final Map<String, String> values;

  /**
   * Конструктор
   *
   * @param values Поля с ошибками
   */
  public JpDocTypeFormatParseException(Map<String, String> values) {
    super("docTypeFormat.error", "Неверно указаны данные документа");
    this.values = Collections.unmodifiableMap(values);
  }

  public Map<String, String> getValues() {
    return values;
  }
}
