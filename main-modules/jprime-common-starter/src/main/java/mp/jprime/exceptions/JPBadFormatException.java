package mp.jprime.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ошибка формата
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JPBadFormatException extends JPRuntimeException {
  public JPBadFormatException() {
    super("format.error", "Ошибка формата данных");
  }
}
