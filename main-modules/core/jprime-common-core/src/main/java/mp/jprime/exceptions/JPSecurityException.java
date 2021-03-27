package mp.jprime.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ошибка безопасности
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public abstract class JPSecurityException extends JPAppRuntimeException {

  /**
   * Создание исключительной ситуации без параметров
   */
  public JPSecurityException() {
    super();
  }

  /**
   * Создание исключительной ситуации с сообщением об ошибке
   *
   * @param messageCode Код ошибки
   * @param message     Ошибка
   */
  public JPSecurityException(String messageCode, String message) {
    super(messageCode, message);
  }
}
