package mp.jprime.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ошибка построения запроса
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class JPQueryException extends JPRuntimeException {
  public JPQueryException() {
    super("query.error", "Ошибка выполнения запроса");
  }
}
