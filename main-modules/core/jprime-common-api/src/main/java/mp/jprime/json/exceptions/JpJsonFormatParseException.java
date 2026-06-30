package mp.jprime.json.exceptions;

import mp.jprime.exceptions.JPAppRuntimeException;

import java.util.Collection;
import java.util.Collections;

/**
 * Ошибка форматирования данных
 */
public class JpJsonFormatParseException extends JPAppRuntimeException {
  private final Collection<String> fields;

  /**
   * Конструктор
   *
   * @param fields Поля с ошибками
   */
  public JpJsonFormatParseException(Collection<String> fields) {
    super("stringFormat.error", "Неверно указано значение");
    this.fields = Collections.unmodifiableCollection(fields);
  }

  public Collection<String> getFields() {
    return fields;
  }
}
