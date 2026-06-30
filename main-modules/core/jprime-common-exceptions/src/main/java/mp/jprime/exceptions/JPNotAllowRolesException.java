package mp.jprime.exceptions;

import mp.jprime.exceptions.enums.JPRoleError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Нет допустимых ролей
 */
@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class JPNotAllowRolesException extends JPAppRuntimeException {
  public JPNotAllowRolesException() {
    super(JPRoleError.INVALID_ALLOW_ROLES.getCode(), JPRoleError.INVALID_ALLOW_ROLES.getName());
  }
}
