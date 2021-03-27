package mp.jprime.utils.exceptions;

import mp.jprime.exceptions.JPRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Шаг утилиты не найден
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JPUtilModeNotFoundException extends JPRuntimeException {
  private final String utilCode;
  private final String modeCode;

  public JPUtilModeNotFoundException() {
    super("mode not found");
    this.utilCode = null;
    this.modeCode = null;
  }

  public JPUtilModeNotFoundException(String utilCode, String modeCode) {
    super("mode " + modeCode + " for jpUtil " + utilCode + " not found");
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