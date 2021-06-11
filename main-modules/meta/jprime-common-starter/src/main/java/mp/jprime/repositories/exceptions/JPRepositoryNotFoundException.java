package mp.jprime.repositories.exceptions;

import mp.jprime.exceptions.JPRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Репозиторий не найден
 */
@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class JPRepositoryNotFoundException extends JPRuntimeException {
  public JPRepositoryNotFoundException() {
    super("repository not found");
  }

  public JPRepositoryNotFoundException(String dbCode) {
    super(dbCode + " repository not found");
  }

  public JPRepositoryNotFoundException(String where, String dbCode) {
    super("repository with code '" + dbCode + "' not found in the " + where);
  }
}
