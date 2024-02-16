package mp.jprime.dataaccess.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Логические условия
 */
public enum BooleanCondition {
  /**
   * Условие И
   */
  AND("AND"),
  /**
   * Условие ИЛИ
   */
  OR("OR");

  private static final Map<String, BooleanCondition> BY_CODE = new HashMap<>();

  static {
    for (BooleanCondition type : BooleanCondition.values()) {
      BY_CODE.put(type.getCode().toLowerCase(), type);
    }
  }


  private final String code;

  BooleanCondition(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  /**
   * Возвращает условие по коду
   *
   * @param code Код
   * @return Логическое условие
   */
  public static BooleanCondition getCond(String code) {
    return code == null ? null : BY_CODE.get(code.toLowerCase());
  }
}
