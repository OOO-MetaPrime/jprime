package mp.jprime.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Метаатрибут не найден
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "jpAttr by code not found")
public class JPAttrNotFoundException extends JPRuntimeException {
  public JPAttrNotFoundException() {
    super("jpAttr.notFound", "Атрибут не найден");
  }

  public JPAttrNotFoundException(String classCode, String attrCode) {
    super(
        "jpAttr." + classCode + "." + attrCode + ".notFound",
        "Атрибут " + classCode + "." + attrCode + " не найден"
    );
  }
}
