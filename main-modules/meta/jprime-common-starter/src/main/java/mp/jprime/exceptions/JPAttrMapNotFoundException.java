package mp.jprime.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Привязка метаатрибута не найдена
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class JPAttrMapNotFoundException extends JPRuntimeException {
  public JPAttrMapNotFoundException() {
    super("jpAttr.mapping.notFound", "Маппинг атрибута не найден");
  }

  public JPAttrMapNotFoundException(String classCode, String attrCode) {
    super(
        "jpAttr." + classCode + "." + attrCode + ".mapping.notFound",
        "Маппинг атрибута " + classCode + "." + attrCode + " не найден"
    );
  }
}
