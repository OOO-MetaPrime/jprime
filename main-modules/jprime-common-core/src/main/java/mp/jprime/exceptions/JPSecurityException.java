package mp.jprime.exceptions;

import mp.jprime.exceptions.JPRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ошибка безопасности
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public abstract class JPSecurityException extends JPRuntimeException {

  /**
   * Создание исключительной ситуации без параметров
   */
  public JPSecurityException() {
    super();
  }

  /**
   * Создание исключительной ситуации с сообщением об ошибке
   *
   * @param message сообщение
   */
  public JPSecurityException(String message) {
    super(message);
  }
}
