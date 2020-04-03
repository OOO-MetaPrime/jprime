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
}
