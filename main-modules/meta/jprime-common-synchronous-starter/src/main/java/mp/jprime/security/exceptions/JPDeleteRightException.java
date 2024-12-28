package mp.jprime.security.exceptions;

import mp.jprime.exceptions.JPSecurityException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Нет прав на удаление
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class JPDeleteRightException extends JPSecurityException {
  private final String classCode;

  public JPDeleteRightException(String classCode) {
    super(classCode + ".delete.denied", classCode + " no delete permission");
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
