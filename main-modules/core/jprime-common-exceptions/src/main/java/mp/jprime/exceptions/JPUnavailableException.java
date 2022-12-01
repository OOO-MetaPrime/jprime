package mp.jprime.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ошибка недоступности
 */
@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class JPUnavailableException extends JPAppRuntimeException {

  /**
   * Создание исключительной ситуации без параметров
   */
  public JPUnavailableException() {
    super();
  }

  /**
   * Создание исключительной ситуации с сообщением об ошибке
   *
   * @param messageCode Код ошибки
   * @param message     Ошибка
   */
  public JPUnavailableException(String messageCode, String message) {
    super(messageCode, message);
  }
}
