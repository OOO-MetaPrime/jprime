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
   * Порядок загрузки кешей в DESC
   *
   * @return Порядок загрузки
   */
  default int getOrder() {
    return 0;
  }

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

  /**
   * Признак необходимости остановки приложения в случае ошибки при первичной загрузке кэша
   */
  default boolean isFailFastOnStartUp() {
    return false;
  }
}
