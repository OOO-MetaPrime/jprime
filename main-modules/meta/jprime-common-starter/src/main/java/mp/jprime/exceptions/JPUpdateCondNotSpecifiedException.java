package mp.jprime.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Условие обновления объектов не указано
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JPUpdateCondNotSpecifiedException extends JPRuntimeException {
  public JPUpdateCondNotSpecifiedException() {
    super("jpConditionalUpdate.where.notSpecified", "Условие обновления объектов не указано");
  }
}
