package mp.jprime.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Метакласс не найден
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "jpClass by code not found")
public class JPClassNotFoundException extends JPRuntimeException {
  public JPClassNotFoundException() {
    super("jpClass.notFound", "Метаописание не найдено");
  }

  public JPClassNotFoundException(String classCode) {
    super("jpClass." + classCode + ".notFound", "Метаописание " + classCode + " не найдено");
  }
}
