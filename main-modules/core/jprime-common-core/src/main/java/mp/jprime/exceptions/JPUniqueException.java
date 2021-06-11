package mp.jprime.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Нарушена уникальность
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class JPUniqueException extends JPRuntimeException {
  public JPUniqueException() {
    super("query.unique.duplicateError", "Нарушена уникальность значений");
  }
}
