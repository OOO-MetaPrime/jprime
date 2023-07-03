package mp.jprime.lang;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public interface JPMap {
  /**
   * Признак наличия данных
   *
   * @return Признак наличия
   */
  boolean containsKey(String field);

  /**
   * Возвращает данные
   *
   * @return Данные
   */
  <T> T get(String field);

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

  /**
   * Возвращает данные
   *
   * @return Данные
   */
  Map<String, Object> toMap();

  /**
   * Возвращает множество ключей
   *
   * @return Множество ключей
   */
  Set<String> keySet();

  /**
   * Возвращает коллекцию значений
   *
   * @return Коллекция значений
   */
  Collection<Object> values();
}
