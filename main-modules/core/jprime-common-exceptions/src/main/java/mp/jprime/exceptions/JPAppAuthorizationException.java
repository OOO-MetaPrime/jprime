package mp.jprime.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ошибка авторизации
 */
@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class JPAppAuthorizationException extends JPAppRuntimeException {

  public JPAppAuthorizationException() {
    super();
  }

  /**
   * Конструктор
   *
   * @param messageCode Код ошибки
   * @param message     Ошибка
   */
  public JPAppAuthorizationException(String messageCode, String message) {
    super(messageCode, message);
  }
}
