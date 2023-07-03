package mp.jprime.security.exceptions;

import mp.jprime.exceptions.JPSecurityException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Некорректный токен
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class JPBrokenTokenException extends JPSecurityException {
  /**
   * Создание исключительной ситуации с сообщением об ошибке
   */
  public JPBrokenTokenException() {
    super();
  }

  /**
   * Создание исключительной ситуации с сообщением об ошибке
   *
   * @param message сообщение
   */
  public JPBrokenTokenException(String message) {
    super("brokenToken", message);
  }
}
