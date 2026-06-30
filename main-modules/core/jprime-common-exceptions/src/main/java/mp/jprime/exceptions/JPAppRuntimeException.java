package mp.jprime.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ошибки от уровня бизнес-логики
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class JPAppRuntimeException extends JPRuntimeException {
  /**
   * Создание исключительной ситуации без параметров
   */
  public JPAppRuntimeException() {
    super();
  }

  /**
   * Конструктор
   *
   * @param messageCode Код ошибки
   * @param message     Ошибка
   */
  public JPAppRuntimeException(String messageCode, String message) {
    super(messageCode, message);
  }

  /**
   * Создание исключительной ситуации по коду ошибки и ошибке
   *
   * @param messageCode Код ошибки
   * @param message     Ошибка
   */
  public static JPAppRuntimeException of(String messageCode, String message) {
    return new JPAppRuntimeException(messageCode, message);
  }

  /**
   * Создание исключительной ситуации по описанию ошибки
   *
   * @param message Ошибка
   */
  public static JPAppRuntimeException ofMessage(String message) {
    return new JPAppRuntimeException(message);
  }

  /**
   * Конструктор
   *
   * @param message Ошибка
   */
  public JPAppRuntimeException(String message) {
    super(message);
  }
}
