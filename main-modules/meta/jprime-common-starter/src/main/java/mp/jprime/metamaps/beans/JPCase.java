package mp.jprime.metamaps.beans;

/**
 * Регистр значений
 */
public enum JPCase {
  UPPER("upper", "Верхний регистр"),
  LOWER("lower", "Нижний регистр"),
  ANY("any", "Произвольный (по умолчанию)");

  /**
   * Код типа
   */
  private final String code;
  /**
   * Название типа
   */
  private final String title;


  JPCase(String code,  String title) {
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
    if (code == null) {
      return null;
    }
    for (JPCase v : JPCase.values()) {
      if (v.code.equalsIgnoreCase(code)) {
        return v;
      }
    }
    return ANY;
  }
}