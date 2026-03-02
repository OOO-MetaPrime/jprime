package mp.jprime.parsers.exceptions;

import mp.jprime.exceptions.JPRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ошибка значения
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JPParserNotFoundException extends JPRuntimeException {
  /**
   * Конструктор
   * @param message     Ошибка
   */
  public JPParserNotFoundException(String message) {
    super(message);
  }
}
