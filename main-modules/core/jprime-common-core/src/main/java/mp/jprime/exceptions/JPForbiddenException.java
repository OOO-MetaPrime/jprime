package mp.jprime.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Выполнение запрещено
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class JPForbiddenException extends JPAppRuntimeException {
  /**
   * Выполнение запрещено
   */
  public JPForbiddenException() {
    super();
  }

  /**
   * Конструктор
   *
   * @param messageCode Код ошибки
   * @param message     Ошибка
   */
  public JPForbiddenException(String messageCode, String message) {
    super(messageCode, message);
  }
}
