package mp.jprime.dataaccess;

import mp.jprime.lang.JPMap;
import mp.jprime.meta.JPAttr;

public interface JPAttrData extends JPMap {
  /**
   * Признак наличия данных
   *
   * @return Признак наличия
   */
  default boolean containsKey(String field) {
    return containsAttr(field);
  }

  /**
   * Возвращает признак наличия данных
   *
   * @return Да/Нет
   */
  default boolean containsAttr(JPAttr attr) {
    return containsAttr(attr.getCode());
  }

  /**
   * Возвращает признак наличия данных
   *
   * @return Да/Нет
   */
  boolean containsAttr(String attr);

  /**
   * Возвращает данные
   *
   * @return Данные
   */
  default <T> T get(JPAttr attr) {
    return get(attr.getCode());
  }
}
