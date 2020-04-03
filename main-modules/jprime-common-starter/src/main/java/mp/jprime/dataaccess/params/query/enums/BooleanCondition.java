package mp.jprime.dataaccess.params.query.enums;

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

  private String code;

  BooleanCondition(String code) {
    this.code = code;
  }

  /**
   * Возвращает условие по коду
   *
   * @param code Код
   * @return Логическое условие
   */
  public static BooleanCondition getCond(String code) {
    if (code == null) {
      return null;
    }
    for (BooleanCondition v : BooleanCondition.values()) {
      if (v.code.equalsIgnoreCase(code)) {
        return v;
      }
    }
    return null;
  }
}
