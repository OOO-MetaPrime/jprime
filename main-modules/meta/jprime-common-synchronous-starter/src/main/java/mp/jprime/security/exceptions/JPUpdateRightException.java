package mp.jprime.security.exceptions;

import mp.jprime.exceptions.JPSecurityException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Нет прав на обновление
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class JPUpdateRightException extends JPSecurityException {
  private final String classCode;

  public JPUpdateRightException(String classCode) {
    this(classCode, classCode + " no update permission");
  }

  public JPUpdateRightException(String classCode, String message) {
    super(classCode + ".update.denied", message);
    this.classCode = classCode;
  }

  /**
   * Возвращает кодовое имя класса
   *
   * @return Кодовое имя класса
   */
  public String getClassCode() {
    return classCode;
  }
}
