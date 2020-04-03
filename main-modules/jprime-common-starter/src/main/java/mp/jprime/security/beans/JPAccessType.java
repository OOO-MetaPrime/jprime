package mp.jprime.security.beans;

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


  private String code;

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
    if (code == null) {
      return null;
    }
    for (JPAccessType v : JPAccessType.values()) {
      if (v.code.equalsIgnoreCase(code)) {
        return v;
      }
    }
    return null;
  }
}
