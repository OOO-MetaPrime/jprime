package mp.jprime.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Нет допустимых ролей
 */
@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class JPNotAllowRolesException extends JPAppRuntimeException {
  public JPNotAllowRolesException() {
    super("invalid_allowRoles", "invalid_allowRoles");
  }
}
