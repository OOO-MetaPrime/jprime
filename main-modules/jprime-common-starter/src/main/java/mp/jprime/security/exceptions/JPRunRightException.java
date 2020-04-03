package mp.jprime.security.exceptions;

import mp.jprime.exceptions.JPSecurityException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Нет прав на запуск
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class JPRunRightException extends JPSecurityException {
  private final String classCode;

  public JPRunRightException(String classCode) {
    super(classCode + " no read permission");
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
