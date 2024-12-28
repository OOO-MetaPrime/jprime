package mp.jprime.caches.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

/**
 * Базовая реализация кэша ключ-значение
 */
public abstract class JPBaseMapCache<C, V> extends JPBaseCache {
  private static final Logger LOG = LoggerFactory.getLogger(JPBaseMapCache.class);

  private volatile Map<C, V> cache;

  @Override
  public void load() {
    this.cache = loadCache();
    afterRefresh();
    LOG.info("Cache {} refreshed", getCode());
  }

  public Collection<V> getValues() {
    waitForLoad();
    return cache.values();
  }

  public V getByCode(C code) {
    waitForLoad();
    return cache.get(code);
  }

  /**
   * Действие после обновления кеша
   */
  protected void afterRefresh() {

  }

  /**
   * Получить кэшируемые значения
   */
  protected abstract Map<C, V> loadCache();
}
