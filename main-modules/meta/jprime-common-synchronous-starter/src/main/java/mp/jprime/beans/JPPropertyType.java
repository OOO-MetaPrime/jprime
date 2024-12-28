package mp.jprime.beans;

import mp.jprime.lang.JPJsonNode;
import mp.jprime.meta.beans.JPType;

import java.util.HashMap;
import java.util.Map;

/**
 * Тип свойства
 */
public enum JPPropertyType {
  /**
   * Сложный объект
   */
  ELEMENT("element", "Элемент", JPJsonNode.class),
  /**
   * Массив элементов
   */
  ELEMENT_ARRAY("elementArray", "Массив элементов", JPJsonNode.class),
  /**
   * Длинное целочисленное
   */
  BIGINT(JPType.BIGINT),
  /**
   * Да/Нет
   */
  BOOLEAN(JPType.BOOLEAN),
  /**
   * Дата
   */
  DATE(JPType.DATE),
  /**
   * Полная дата (c учетом часового пояса)
   */
  DATETIME(JPType.DATETIME),
  /**
   * Вещественное (64 бита)
   */
  DOUBLE(JPType.DOUBLE),
  /**
   * Вещественное (32 бита)
   */
  FLOAT(JPType.FLOAT),
  /**
   * Целочисленное (32 бита)
   */
  INT(JPType.INT),
  /**
   * Массив целочисленных (32 бита)
   */
  INT_ARRAY(JPType.INT_ARRAY),
  /**
   * Целочисленное (64 бита)
   */
  LONG(JPType.LONG),
  /**
   * Массив целочисленных (64 бита)
   */
  LONG_ARRAY(JPType.LONG_ARRAY),
  /**
   * Строка
   */
  STRING(JPType.STRING),
  /**
   * Массив строк
   */
  STRING_ARRAY(JPType.STRING_ARRAY),
  /**
   * Полная дата (без учета часового пояса)
   */
  TIMESTAMP(JPType.TIMESTAMP),
  /**
   * Время
   */
  TIME(JPType.TIME),
  /**
   * Глобальный идентификатор
   */
  UUID(JPType.UUID);

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
  /**
   * Java-класс, ставящийся в соответствие к коду типа
   */
  private final Class<?> javaClass;

  JPPropertyType(JPType type) {
    this.code = type.getCode();
    this.title = type.getTitle();
    this.javaClass = type.getJavaClass();
  }

  JPPropertyType(String code, String title, Class<?> javaClass) {
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
   * Название типа
   *
   * @return Название типа
   */
  public String getTitle() {
    return title;
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
   * Возвращает тип свойства по коду
   *
   * @param code Код
   * @return Тип свойства
   */
  public static JPPropertyType getType(String code) {
    return code == null ? null : BY_CODE.get(code.toLowerCase());
  }
}
