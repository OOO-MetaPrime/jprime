package mp.jprime.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Привязка метакласса не найдена
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class JPClassMapNotFoundException extends JPRuntimeException {
  public JPClassMapNotFoundException() {
    super("jpClass.mapping.notFound", "Хранилище не найдено");
  }

  public JPClassMapNotFoundException(String classCode) {
    super("jpClass." + classCode + ".mapping.notFound", "Хранилище не найдено");
  }
}
