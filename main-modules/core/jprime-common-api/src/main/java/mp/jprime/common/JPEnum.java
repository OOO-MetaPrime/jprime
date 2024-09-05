package mp.jprime.common;

/**
 * описания значения
 */
public interface JPEnum {
  /**
   * Значение
   *
   * @return Значение
   */
  Object getValue();

  /**
   * Название значения
   *
   * @return Название
   */
  String getDescription();

  /**
   * Уникальный qName
   *
   * @return Уникальный qName
   */
  String getQName();


  record JPEnumRecord(Object getValue, String getDescription, String getQName) implements JPEnum {

  }

  static JPEnum of(Object value, String description, String qName) {
    return new JPEnumRecord(value, description, qName);
  }

  static JPEnum of(Object value, String description) {
    return new JPEnumRecord(value, description, null);
  }
}
