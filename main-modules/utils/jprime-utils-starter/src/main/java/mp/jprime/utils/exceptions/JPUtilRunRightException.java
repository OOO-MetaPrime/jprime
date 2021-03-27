package mp.jprime.utils.exceptions;

import mp.jprime.exceptions.JPSecurityException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Нет прав на запуск
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class JPUtilRunRightException extends JPSecurityException {
  private final String utilCode;

  public JPUtilRunRightException(String utilCode) {
    super(utilCode + ".run.denied", utilCode + " no run permission");
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
