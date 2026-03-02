package mp.jprime.xml.exceptions;

import mp.jprime.exceptions.JPRuntimeException;

/**
 * Ошибка преобразования из XML
 */
public class JPXmlUnmarshalException extends JPRuntimeException {
  private static final long serialVersionUID = 4832827419448670354L;

  /**
   * Конструктор
   *
   * @param message сообщение
   */
  public JPXmlUnmarshalException(String message) {
    super(message);
  }

  /**
   * Конструктор
   *
   * @param message сообщение
   * @param cause   ошибка
   */
  public JPXmlUnmarshalException(String message, Throwable cause) {
    super(message + ". " + cause.getMessage(), cause);
  }
}
