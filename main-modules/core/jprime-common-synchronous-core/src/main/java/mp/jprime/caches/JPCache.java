package mp.jprime.caches;

/**
 * Кэш
 */
public interface JPCache {
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
