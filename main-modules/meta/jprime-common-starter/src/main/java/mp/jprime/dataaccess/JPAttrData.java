package mp.jprime.dataaccess;

import mp.jprime.meta.JPAttr;

import java.util.function.BiConsumer;

public interface JPAttrData {
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

  /**
   * Возвращает данные
   *
   * @return Данные
   */
  <T> T get(String attr);

  /**
   * Реализация итератора
   *
   * @param action BiConsumer
   */
  void forEach(BiConsumer<? super String, ? super Object> action);

  /**
   * Признак отсутствия данных
   *
   * @return Да/Нет
   */
  boolean isEmpty();

  /**
   * Размер данных
   *
   * @return Размер данных
   */
  int size();
}
