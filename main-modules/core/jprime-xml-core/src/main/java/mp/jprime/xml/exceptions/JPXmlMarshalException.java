package mp.jprime.xml.exceptions;

import mp.jprime.exceptions.JPRuntimeException;

/**
 * Ошибка преобразования в XML
 */
public class JPXmlMarshalException extends JPRuntimeException {
  private static final long serialVersionUID = 5615139443193253142L;

  /**
   * Конструктор
   *
   * @param message сообщение
   */
  public JPXmlMarshalException(String message) {
    super(message);
  }

  /**
   * Конструктор
   *
   * @param message сообщение
   * @param cause   ошибка
   */
  public JPXmlMarshalException(String message, Throwable cause) {
    super(message + ". " + cause.getMessage(), cause);
  }
}
