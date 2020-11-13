package mp.jprime.parsers.exceptions;

import mp.jprime.exceptions.JPAppRuntimeException;
import mp.jprime.exceptions.JPRuntimeException;

public class JPParserNotFoundException extends JPRuntimeException {

  /**
   * Конструктор
   * @param message     Ошибка
   */
  public JPParserNotFoundException(String message) {
    super(message);
  }
}
