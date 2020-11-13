package mp.jprime.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Класс описывающий исключительную ситуацаю, которая возникает в процессе работы
 * и будет передоваться в цепочку вызовов до тех пор, пока не будет обработана
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class JPRuntimeException extends RuntimeException {
  /**
   * Номер версии класса для сериализации
   */
  private static final long serialVersionUID = -3400375416094122984L;

  /**
   * Код мультиязычного сообщения
   */
  private String messageCode;

  /**
   * Код ошибки
   *
   * @return Код ошибки
   */
  public String getMessageCode() {
    return messageCode;
  }

  /**
   * Конструктор
   *
   * @param messageCode Код ошибки
   * @param message     Ошибка
   */
  public JPRuntimeException(String messageCode, String message) {
    super(message);
    this.messageCode = messageCode;
  }

  /**
   * Создание исключительной ситуации без параметров
   */
  public JPRuntimeException() {
    super();
  }

  /**
   * Создание исключительной ситуации с сообщением об ошибке
   *
   * @param message сообщение
   */
  public JPRuntimeException(String message) {
    super(message);
  }

  /**
   * Создание исключительной ситуации по причине другой исключительной
   * ситуации
   *
   * @param cause исключительная ситуация
   */
  public JPRuntimeException(Throwable cause) {
    super(cause.getMessage(), cause);
  }

  /**
   * Создание исключительной ситуации по причине другой исключительной
   * ситуации с дополнительным указанием сообщения
   *
   * @param message сообщение
   * @param cause   исключительная ситуация
   */
  public JPRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

  public static <T extends RuntimeException> RuntimeException wrapException(String message, Exception e, Class<T> eClass) {
    if (e instanceof RuntimeException) {
      return (RuntimeException) e;
    }

    try {
      return eClass.getConstructor(String.class, Throwable.class).newInstance(message, e);
    } catch (Exception ef) {
      return new JPRuntimeException(message, e);
    }
  }

  /**
   * Метод, позволяющий обернуть checked Exception в Runtime, Если он таковым не является
   *
   * @param e Exception
   * @return RuntimeException
   */
  public static RuntimeException wrapException(Exception e) {
    return wrapException(e.getMessage(), e, JPRuntimeException.class);
  }

  /**
   * Метод, позволяющий обернуть checked Exception в Runtime, Если он таковым не является
   *
   * @param message сообщение
   * @param e       Exception
   * @return RuntimeException
   */
  public static RuntimeException wrapException(String message, Exception e) {
    return wrapException(message, e, JPRuntimeException.class);
  }
}
