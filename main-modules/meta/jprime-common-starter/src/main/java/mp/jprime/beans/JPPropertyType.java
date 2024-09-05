package mp.jprime.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * Тип свойства
 */
public enum JPPropertyType {
  /**
   * Сложный объект
   */
  ELEMENT("element", "Элемент"),
  /**
   * Массив элементов
   */
  ELEMENT_ARRAY("elementArray", "Массив элементов"),
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
   * Массив целочисленных (32 бита)
   */
  INT_ARRAY("intArray", "Массив целочисленных (32 бита)"),
  /**
   * Целочисленное (64 бита)
   */
  LONG("long", "Целочисленное (64 бита)"),
  /**
   * Массив целочисленных (64 бита)
   */
  LONG_ARRAY("longArray", "Массив целочисленных (64 бита)"),
  /**
   * Строка
   */
  STRING("string", "Строка"),
  /**
   * Массив строк
   */
  STRING_ARRAY("stringArray", "Массив строк"),
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

  private static final Map<String, JPPropertyType> BY_CODE = new HashMap<>();

  static {
    for (JPPropertyType type : JPPropertyType.values()) {
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


  JPPropertyType(String code, String title) {
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
  public static JPPropertyType getType(String code) {
    return code == null ? null : BY_CODE.get(code.toLowerCase());
  }
}
