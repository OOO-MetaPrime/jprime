package mp.jprime.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Условие удаления объектов не указано
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JPDeleteCondNotSpecifiedException extends JPRuntimeException {
  public JPDeleteCondNotSpecifiedException() {
    super("jpConditionalDelete.where.notSpecified", "Условие удаления объектов не указано");
  }
}
