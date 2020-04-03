package mp.jprime.security.exceptions;

import mp.jprime.exceptions.JPSecurityException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Нет прав на создание
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class JPCreateRightException extends JPSecurityException {
  private final String classCode;

  public JPCreateRightException(String classCode) {
    super(classCode + " no create permission");
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
