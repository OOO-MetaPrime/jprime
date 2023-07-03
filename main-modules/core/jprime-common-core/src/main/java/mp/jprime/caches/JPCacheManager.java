package mp.jprime.caches;

/**
 * Сервис управления {@link JPCache кэшами}
 */
public interface JPCacheManager {
  /**
   * Обновить кэш по коду
   *
   * @param code код кэша
   */
  void refresh(String code);

}
