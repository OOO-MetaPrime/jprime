package mp.jprime.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Тип атрибута не определен
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "jpAttrType not found")
public class JPAttrTypeNotFoundException extends JPRuntimeException {
  public JPAttrTypeNotFoundException() {
    super("jpAttrType.notFound", "Тип атрибута не определен");
  }

  public JPAttrTypeNotFoundException(String classCode, String attrCode) {
    super(
        "jpAttrType." + classCode + "." + attrCode + ".notFound",
        "Тип атрибута " + classCode + "." + attrCode + " не определен"
    );
  }
}
