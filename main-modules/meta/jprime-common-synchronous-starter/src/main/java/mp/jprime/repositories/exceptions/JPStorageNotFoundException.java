package mp.jprime.repositories.exceptions;

import mp.jprime.exceptions.JPRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Хранилище не найдено
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class JPStorageNotFoundException extends JPRuntimeException {

  public JPStorageNotFoundException() {
    super("storage not found");
  }

  /**
   * @param code код хранилища
   */
  public JPStorageNotFoundException(String code) {
    super("storage with code '" + code + "' not found");
  }
}
