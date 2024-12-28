package mp.jprime.security.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * Типы доступа
 */
public enum JPAccessType {
  /**
   * Разрешительный
   */
  PERMIT("permit"),
  /**
   * Запретительный
   */
  PROHIBITION("prohibition");

  private static final Map<String, JPAccessType> BY_CODE = new HashMap<>();

  static {
    for (JPAccessType type : JPAccessType.values()) {
      BY_CODE.put(type.getCode().toLowerCase(), type);
    }
  }

  private final String code;

  JPAccessType(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  @Override
  public String toString() {
    return "JPAccessType{" +
        "code='" + code + '\'' +
        '}';
  }

  /**
   * Возвращает тип доступа
   *
   * @param code Код
   * @return тип доступа
   */
  public static JPAccessType getType(String code) {
    return code == null ? null : BY_CODE.get(code.toLowerCase());
  }
}
