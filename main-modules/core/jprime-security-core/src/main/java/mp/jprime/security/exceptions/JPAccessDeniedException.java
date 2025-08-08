package mp.jprime.security.exceptions;

import mp.jprime.exceptions.JPSecurityException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ошибка доступа
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class JPAccessDeniedException extends JPSecurityException {
  /**
   * Создание исключительной ситуации с сообщением об ошибке
   */
  public JPAccessDeniedException() {
    super();
  }

  /**
   * Создание исключительной ситуации с сообщением об ошибке
   *
   * @param message сообщение
   */
  public JPAccessDeniedException(String message) {
    super("accessDenied", message);
  }
}
