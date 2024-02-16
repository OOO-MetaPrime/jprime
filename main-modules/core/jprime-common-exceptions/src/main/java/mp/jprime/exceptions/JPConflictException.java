package mp.jprime.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ошибки работы с актуальностью данных
 */
@ResponseStatus(code = HttpStatus.CONFLICT)
public class JPConflictException extends JPAppRuntimeException {

  /**
   * Конструктор
   *
   * @param messageCode Код ошибки
   * @param message     Ошибка
   */
  public JPConflictException(String messageCode, String message) {
    super(messageCode, message);
  }

  /**
   * Конструктор
   */
  public JPConflictException() {
    this("data.conflict", "Ошибка в данных");
  }
}
