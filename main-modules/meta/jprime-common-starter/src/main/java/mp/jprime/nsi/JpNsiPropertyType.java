package mp.jprime.nsi;

import mp.jprime.meta.beans.JPType;

import java.util.HashMap;
import java.util.Map;

/**
 * Тип поля НСИ справочника
 */
public enum JpNsiPropertyType {
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
   * Целочисленное (32 бита)
   */
  INT(JPType.INT),
  /**
   * Массив целочисленных (32 бита)
   */
  INT_ARRAY(JPType.INT_ARRAY),
  /**
   * Строка
   */
  STRING(JPType.STRING),
  /**
   * Массив строк
   */
  STRING_ARRAY(JPType.STRING_ARRAY);

  private static final Map<String, JpNsiPropertyType> BY_CODE = new HashMap<>();

  static {
    for (JpNsiPropertyType type : JpNsiPropertyType.values()) {
      BY_CODE.put(type.getCode().toLowerCase(), type);
    }
  }

  /**
   * JPType type
   */
  private final JPType type;

  JpNsiPropertyType(JPType type) {
    this.type = type;
  }

  /**
   * Возвращает тип атрибута по коду
   *
   * @param code Код
   * @return Тип атрибута
   */
  public static JpNsiPropertyType getType(String code) {
    return code == null ? null : BY_CODE.get(code.toLowerCase());
  }

  /**
   * Возвращает код
   *
   * @return Код
   */
  public String getCode() {
    return type.getCode();
  }

  /**
   * Название типа
   *
   * @return Название типа
   */
  public String getTitle() {
    return type.getTitle();
  }

  /**
   * Возвращает java-класс
   *
   * @return Java-класс
   */
  public Class getJavaClass() {
    return type.getJavaClass();
  }
}
