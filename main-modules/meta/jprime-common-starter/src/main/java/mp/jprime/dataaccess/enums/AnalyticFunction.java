package mp.jprime.dataaccess.enums;

import java.util.HashMap;
import java.util.Map;

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

  private static final Map<String, AnalyticFunction> BY_CODE = new HashMap<>();

  static {
    for (AnalyticFunction type : AnalyticFunction.values()) {
      BY_CODE.put(type.getCode().toLowerCase(), type);
    }
  }

  private final String code;

  AnalyticFunction(String code) {
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
  public static AnalyticFunction getCond(String code) {
    return code == null ? null : BY_CODE.get(code.toLowerCase());
  }
}
