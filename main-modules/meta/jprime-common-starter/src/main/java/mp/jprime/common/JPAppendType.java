package mp.jprime.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Тип привязки
 */
public enum JPAppendType {
  /**
   * Обработка 1 объекта
   */
  OBJECT("object"),
  /**
   * Обработка списка объектов
   */
  LIST("list"),
  /**
   * Обработка объекта и списка объектов
   */
  UNI("uni"),
  /**
   * Произвольная
   */
  CUSTOM("custom");

  private static final Map<String, JPAppendType> BY_CODE = new HashMap<>();

  static {
    for (JPAppendType type : JPAppendType.values()) {
      BY_CODE.put(type.getCode().toLowerCase(), type);
    }
  }

  /**
   * Код типа утилиты
   */
  private final String code;

  JPAppendType(String code) {
    this.code = code;
  }

  /**
   * Код типа утилиты
   *
   * @return Код типа утилита
   */
  public String getCode() {
    return code;
  }

  /**
   * Возвращает тип привязки
   *
   * @param code Код
   * @return Тип привязки
   */
  public static JPAppendType getType(String code) {
    return code == null ? null : BY_CODE.get(code.toLowerCase());
  }
}
