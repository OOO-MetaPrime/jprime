package mp.jprime.beans;

/**
 * Тип свойства
 */
public enum PropertyType {
  /**
   * Сложный объект
   */
  ELEMENT("element", "Элемент"),
  /**
   * Длинное целочисленное
   */
  BIGINT("biginteger", "Длинное целочисленное"),
  /**
   * Да/Нет
   */
  BOOLEAN("boolean", "Да/Нет"),
  /**
   * Дата
   */
  DATE("date", "Дата"),
  /**
   * Полная дата (c учетом часового пояса)
   */
  DATETIME("datetime", "Полная дата (c учетом часового пояса)"),
  /**
   * Вещественное (64 бита)
   */
  DOUBLE("double", "Вещественное (64 бита)"),
  /**
   * Вещественное (32 бита)
   */
  FLOAT("float", "Вещественное (32 бита)"),
  /**
   * Целочисленное (32 бита)
   */
  INT("integer", "Целочисленное (32 бита)"),
  /**
   * Целочисленное (64 бита)
   */
  LONG("long", "Целочисленное (64 бита)"),
  /**
   * Строка
   */
  STRING("string", "Строка"),
  /**
   * Полная дата (без учета часового пояса)
   */
  TIMESTAMP("timestamp", "Полная дата(без учета часового пояса) "),
  /**
   * Время
   */
  TIME("time", "Время"),
  /**
   * Глобальный идентификатор
   */
  UUID("uuid", "Глобальный идентификатор");


  /**
   * Код типа
   */
  private final String code;
  /**
   * Название типа
   */
  private final String title;


  PropertyType(String code, String title) {
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
   * Возвращает тип свойства по коду
   *
   * @param code Код
   * @return Тип свойства
   */
  public static PropertyType getType(String code) {
    if (code == null) {
      return null;
    }
    for (PropertyType v : PropertyType.values()) {
      if (v.code.equalsIgnoreCase(code)) {
        return v;
      }
    }
    return null;
  }
}
