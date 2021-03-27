package mp.jprime.utils.exceptions;

import mp.jprime.exceptions.JPSecurityException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Нет прав на запуск
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class JPUtilModeRunRightException extends JPSecurityException {
  private final String utilCode;
  private final String modeCode;

  public JPUtilModeRunRightException(String utilCode, String modeCode) {
    super(utilCode + "." + modeCode + ".run.denied", "mode " + modeCode + " for jpUtil " + utilCode + " no run permission");
    this.utilCode = utilCode;
    this.modeCode = modeCode;
  }

  /**
   * Возвращает кодовое имя утилиты
   *
   * @return Кодовое имя утилиты
   */
  public String getUtilCode() {
    return utilCode;
  }

  /**
   * Возвращает кодовое имя шага
   *
   * @return Кодовое имя шага
   */
  public String getModeCode() {
    return modeCode;
  }
}
