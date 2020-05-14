package mp.jprime.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Данные не найдены
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JPNotFoundException extends JPAppRuntimeException {
  /**
   * Конструктор
   *
   * @param messageCode Код ошибки
   * @param message     Ошибка
   */
  public JPNotFoundException(String messageCode, String message) {
    super(messageCode, message);
  }

  /**
   * Конструктор
   */
  public JPNotFoundException() {
    this("data.notFound", "Данные не найдены");
  }
}
