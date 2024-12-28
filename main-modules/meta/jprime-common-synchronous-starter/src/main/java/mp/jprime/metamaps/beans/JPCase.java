package mp.jprime.metamaps.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * Регистр значений
 */
public enum JPCase {
  UPPER("upper", "Верхний регистр"),
  LOWER("lower", "Нижний регистр"),
  ANY("any", "Произвольный (по умолчанию)");

  private static final Map<String, JPCase> BY_CODE = new HashMap<>();

  static {
    for (JPCase type : JPCase.values()) {
      BY_CODE.put(type.getCode().toLowerCase(), type);
    }
  }

  /**
   * Код типа
   */
  private final String code;
  /**
   * Название типа
   */
  private final String title;


  JPCase(String code, String title) {
    this.code = code;
    this.title = title;
  }

  /**
   * Возвращает код
   *
   * @return Код
   */
  public String getCode() {
    return code;
  }

  /**
   * Название типа
   *
   * @return Название типа
   */
  public String getTitle() {
    return title;
  }


  /**
   * Возвращает регистр значений по коду
   *
   * @param code Код
   * @return регистр значений
   */
  public static JPCase getCase(String code) {
    JPCase result = code == null ? null : BY_CODE.get(code.toLowerCase());
    return result != null ? result : ANY;
  }
}