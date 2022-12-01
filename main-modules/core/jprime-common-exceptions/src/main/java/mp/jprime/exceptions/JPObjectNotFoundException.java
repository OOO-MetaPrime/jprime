package mp.jprime.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Объект не найдет
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JPObjectNotFoundException extends JPRuntimeException {
  public JPObjectNotFoundException() {
    super("jpObject.notFound", "Объект не найден");
  }
}
