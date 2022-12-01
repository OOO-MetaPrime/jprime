package mp.jprime.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Превышение времени ожидания запроса
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class JPTimeoutException extends JPRuntimeException {
  public JPTimeoutException() {
    super("query.timeout.exceeded", "Время выполнения запроса превышено");
  }
}
