package mp.jprime.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Метаатрибут-идентификатор не найден
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "jpAttr primary key not found")
public class JPPrimaryAttrNotFoundException extends JPAttrNotFoundException {
  public JPPrimaryAttrNotFoundException(String classCode) {
    super("jpAttr." + classCode + ".primaryKey.notFound", "Ключевой атрибут " + classCode + " не найден");
  }
}
