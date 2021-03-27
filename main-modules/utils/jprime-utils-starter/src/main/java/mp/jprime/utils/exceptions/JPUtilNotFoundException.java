package mp.jprime.utils.exceptions;

import mp.jprime.exceptions.JPRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Утилита не найдена
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JPUtilNotFoundException extends JPRuntimeException {
  private final String utilCode;

  public JPUtilNotFoundException() {
    super("jpUtil not found");
    this.utilCode = null;
  }

  public JPUtilNotFoundException(String utilCode) {
    super("jpUtil " + utilCode + " not found");
    this.utilCode = utilCode;
  }

  /**
   * Возвращает кодовое имя утилиты
   *
   * @return Кодовое имя утилиты
   */
  public String getUtilCode() {
    return utilCode;
  }
}
