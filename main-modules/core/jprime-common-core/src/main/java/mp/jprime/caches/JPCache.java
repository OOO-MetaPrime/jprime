package mp.jprime.caches;

import java.util.Collection;

/**
 * Кэш
 *
 * @param <C> тип кода в кэше
 * @param <V> тип значения в кэше
 */
public interface JPCache<C, V> {
  /**
   * Код кэша
   */
  String getCode();

  /**
   * Получить все кэшированные значения
   */
  Collection<V> getValues();

  /**
   * Получить кэшированное значение по коду
   *
   * @param code код
   */
  V getByCode(C code);


  /**
   * Обновить кэш
   */
  void refresh();
}
