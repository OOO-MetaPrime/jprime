package mp.jprime.meta.beans;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
/**
 * Тип атрибута
 */
public enum JPType {
  /**
   * Обратная ссылка
   */
  BACKREFERENCE("backReference", "Обратная ссылка", null),
  /**
   * Длинное целочисленное
   */
  BIGINT("biginteger", "Длинное целочисленное", BigInteger.class),
  /**
   * Да/Нет
   */
  BOOLEAN("boolean", "Да/Нет", Boolean.class),
  /**
   * Дата
   */
  DATE("date", "Дата", LocalDate.class),
  /**
   * Полная дата (без учета часового пояса)
   */
  DATETIME("datetime", "Полная дата (без учета часового пояса)", LocalDateTime.class),
  /**
   * Вещественное (64 бита)
   */
  DOUBLE("double", "Вещественное (64 бита)", Double.class),
  /**
   * Файл
   */
  FILE("file", "Файл", String.class),
  /**
   * Вещественное (32 бита)
   */
  FLOAT("float", "Вещественное (32 бита)", Float.class),
  /**
   * Целочисленное (32 бита)
   */
  INT("integer", "Целочисленное (32 бита)", Integer.class),
  /**
   * JSONa
   */
  JSON("json", "JSON", String.class),
  /**
   * Целочисленное (64 бита)
   */
  LONG("long", "Целочисленное (64 бита)", Long.class),
  /**
   * Пустой тип
   */
  NONE("none", "Не определен", null),
  /**
   * Строка
   */
  STRING("string", "Строка", String.class),
  /**
   * Полная дата (c учетом часового пояса)
   */
  TIMESTAMP("timestamp", "Полная дата (c учетом часового пояса)", LocalDateTime.class),
  /**
   * Время
   */
  TIME("time", "Время", LocalTime.class),
  /**
   * Глобальный идентификатор
   */
  UUID("uuid", "Глобальный идентификатор", java.util.UUID.class),
  /**
   * XML-элемент
   */
  XML("xml", "XML-элемент", String.class),
  /**
   * Виртуальная ссылка
   */
  VIRTUALREFERENCE("virtualReference", "Виртуальная ссылка", null);

  /**
   * Код типа
   */
  private final String code;
  /**
   * Название типа
   */
  private final String title;

  /**
   * Java-класс, ставящийся в соответствие к коду типа
   */
  private final Class javaClass;

  JPType(String code, String title, Class javaClass) {
    this.code = code;
    this.title = title;
    this.javaClass = javaClass;
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
   * Возвращает java-класс
   *
   * @return Java-класс
   */
  public Class getJavaClass() {
    return javaClass;
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
   * Возвращает java-класс по коду
   *
   * @return Java-класс
   */
  public static Class getJavaClassByCode(String code) {
    if (code == null) {
      return null;
    }

    for (JPType jpType : JPType.values()) {
      if (code.equalsIgnoreCase(jpType.code)) {
        return jpType.javaClass;
      }
    }

    return null;
  }

  /**
   * Возвращает тип атрибута по коду
   *
   * @param code Код
   * @return Тип атрибута
   */
  public static JPType getType(String code) {
    if (code == null) {
      return null;
    }
    for (JPType v : JPType.values()) {
      if (v.code.equalsIgnoreCase(code)) {
        return v;
      }
    }
    return null;
  }
}
