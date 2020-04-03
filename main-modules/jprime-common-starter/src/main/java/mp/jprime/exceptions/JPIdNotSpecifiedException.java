package mp.jprime.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Идентификатор не указан
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JPIdNotSpecifiedException extends JPRuntimeException {
  public JPIdNotSpecifiedException() {
    super("jpId.notSpecified", "Идентификатор не определен");
  }
}
