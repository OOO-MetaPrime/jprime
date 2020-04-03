package mp.jprime.parsers.exceptions;

import mp.jprime.exceptions.JPAppRuntimeException;

/**
 * Ошибка форматирования данных
 */
public class JPParseException extends JPAppRuntimeException {
  /**
   * Конструктор
   *
   * @param messageCode Код ошибки
   * @param message     Ошибка
   */
  public JPParseException(String messageCode, String message) {
    super(messageCode, message);
  }
}
