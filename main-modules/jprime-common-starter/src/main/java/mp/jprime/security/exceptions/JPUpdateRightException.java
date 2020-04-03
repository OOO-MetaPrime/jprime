package mp.jprime.security.exceptions;

import mp.jprime.exceptions.JPSecurityException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Нет прав на чтение
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class JPUpdateRightException extends JPSecurityException {
  private final String classCode;

  public JPUpdateRightException(String classCode) {
    super(classCode + " no update permission");
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
