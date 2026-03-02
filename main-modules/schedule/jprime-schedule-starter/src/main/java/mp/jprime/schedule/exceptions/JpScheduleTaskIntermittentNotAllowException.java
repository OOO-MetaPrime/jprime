package mp.jprime.schedule.exceptions;

import mp.jprime.exceptions.JPAppRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Нет возможности остановить/запустить задачу
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JpScheduleTaskIntermittentNotAllowException extends JPAppRuntimeException {
  public JpScheduleTaskIntermittentNotAllowException() {
    super("intermittentNotAllow", "Нет возможности остановить/запустить задачу");
  }
}
