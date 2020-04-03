package mp.jprime.exceptions;

import mp.jprime.dataaccess.params.query.enums.FilterOperation;

/**
 * Некорректное условие на атрибут
 */
public class JPAttrBadCondException extends JPRuntimeException {
  public JPAttrBadCondException() {
    super("jpAttr.badCond", "Некорректное условие на атрибут");
  }

  public JPAttrBadCondException(String classCode, String attrCode, FilterOperation cond) {
    super(
        "jpAttr." + classCode + "." + attrCode + "." + cond.name() + ".badCond",
        "Некорректное условие " + cond.name() + " на атрибут " + classCode + "." + attrCode
    );
  }
}
