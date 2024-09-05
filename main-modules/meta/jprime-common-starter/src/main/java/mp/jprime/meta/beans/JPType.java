package mp.jprime.meta.beans;

import mp.jprime.lang.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

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
   * Диапазон дат
   */
  DATE_RANGE("dateRange", "Диапазон дат", JPDateRange.class),
  /**
   * Полная дата (c учетом часового пояса)
   */
  DATETIME("datetime", "Полная дата (c учетом часового пояса)", LocalDateTime.class),
  /**
   * Диапазон полных дат
   */
  DATETIME_RANGE("datetimeRange", "Диапазон полных дат", JPDateTimeRange.class),
  /**
   * Вещественное (64 бита)
   */
  DOUBLE("double", "Вещественное (64 бита)", BigDecimal.class),
  /**
   * Файл
   */
  FILE("file", "Файл", String.class),
  /**
   * Вещественное (32 бита)
   */
  FLOAT("float", "Вещественное (32 бита)", BigDecimal.class),
  /**
   * Геометрия
   */
  GEOMETRY("geometry", "Геометрия", JPGeometry.class),
  /**
   * Целочисленное (32 бита)
   */
  INT("integer", "Целочисленное (32 бита)", Integer.class),
  /**
   * Массив целочисленных (32 бита)
   */
  INT_ARRAY("intArray", "Массив целочисленных (32 бита)", JPIntegerArray.class),
  /**
   * Диапазон целочисленный (32 бита)
   */
  INT_RANGE("intRange", "Диапазон целочисленный (32 бита)", JPIntegerRange.class),
  /**
   * JSON
   */
  JSON("json", "JSON", String.class),
  /**
   * Целочисленное (64 бита)
   */
  LONG("long", "Целочисленное (64 бита)", Long.class),
  /**
   * Массив целочисленных (64 бита)
   */
  LONG_ARRAY("longArray", "Массив целочисленных (64 бита)", JPLongArray.class),
  /**
   * Денежное
   */
  MONEY("money", "Денежное", BigDecimal.class),
  /**
   * Пустой тип
   */
  NONE("none", "Не определен", null),
  /**
   * Простая дробь
   */
  SIMPLEFRACTION("simpleFraction", "Простая дробь", Integer.class),
  /**
   * Строка
   */
  STRING("string", "Строка", String.class),
  /**
   * Массив строк
   */
  STRING_ARRAY("stringArray", "Массив строк", JPStringArray.class),
  /**
   * Полная дата (без учета часового пояса)
   */
  TIMESTAMP("timestamp", "Полная дата (без учета часового пояса)", LocalDateTime.class),
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
   * Виртуальное значение
   */
  VIRTUALREFERENCE("virtualReference", "Виртуальное значение", null);

  private static final Map<String, JPType> BY_CODE = new HashMap<>();

  static {
    for (JPType type : JPType.values()) {
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

  /**
   * Java-класс, ставящийся в соответствие к коду типа
   */
  private final Class<?> javaClass;

  JPType(String code, String title, Class<?> javaClass) {
    this.code = code;
    this.title = title;
    this.javaClass = javaClass;
  }

  /**
   * Возвращает код
   *
   * @return Код
   */
  public static String getCode(JPType type) {
    return type == null ? null : type.getCode();
  }

  /**
   * Возвращает тип атрибута по коду
   *
   * @param code Код
   * @return Тип атрибута
   */
  public static JPType getType(String code) {
    return code == null ? null : BY_CODE.get(code.toLowerCase());
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
  public Class<?> getJavaClass() {
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
}
