package mp.jprime.common;

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
    if (code == null) {
      return null;
    }
    for (JPAppendType v : JPAppendType.values()) {
      if (v.code.equalsIgnoreCase(code)) {
        return v;
      }
    }
    return null;
  }
}
