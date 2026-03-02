package mp.jprime.common;

import mp.jprime.dataaccess.params.query.Filter;

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

  /**
   * Условие отображения значения
   *
   * @return Условие
   */
  Filter getViewCond();


  record JPEnumRecord(Object getValue, String getDescription, String getQName, Filter getViewCond) implements JPEnum {

  }

  static JPEnum of(Object value, String description, String qName, Filter viewCond) {
    return new JPEnumRecord(value, description, qName, viewCond);
  }

  static JPEnum of(Object value, String description, String qName) {
    return new JPEnumRecord(value, description, qName, null);
  }

  static JPEnum of(Object value, String description) {
    return new JPEnumRecord(value, description, null, null);
  }
}
