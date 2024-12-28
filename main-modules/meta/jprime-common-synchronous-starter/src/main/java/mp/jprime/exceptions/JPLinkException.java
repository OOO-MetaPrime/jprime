package mp.jprime.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Неверно указанны связи
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class JPLinkException extends JPRuntimeException {
  public JPLinkException() {
    super();
  }
}
