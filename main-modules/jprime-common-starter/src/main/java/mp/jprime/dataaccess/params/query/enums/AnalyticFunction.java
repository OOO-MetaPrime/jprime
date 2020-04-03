package mp.jprime.dataaccess.params.query.enums;

/**
 * Аналитические функции
 */
public enum AnalyticFunction {
  /**
   * Существует
   */
  EXISTS("EXISTS"),
  /**
   * Не существует
   */
  NOTEXISTS("NOTEXISTS");

  private String code;

  AnalyticFunction(String code) {
    this.code = code;
  }

  /**
   * Возвращает условие по коду
   *
   * @param code Код
   * @return Логическое условие
   */
  public static AnalyticFunction getCond(String code) {
    if (code == null) {
      return null;
    }
    for (AnalyticFunction v : AnalyticFunction.values()) {
      if (v.code.equalsIgnoreCase(code)) {
        return v;
      }
    }
    return null;
  }
}
