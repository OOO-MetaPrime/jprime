package mp.jprime.caches.services;

import mp.jprime.caches.JPCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

/**
 * Базовая реализация кэша
 */
public abstract class JPBaseCache<C, V> implements JPCache<C, V> {
  private static final Logger LOG = LoggerFactory.getLogger(JPBaseCache.class);

  private volatile Map<C, V> cache;

  @Override
  public void refresh() {
    this.cache = loadCache();
    LOG.info("Cache {} refreshed", getCode());
    afterRefresh();
  }

  @Override
  public Collection<V> getValues() {
    if (cache == null) {
      refresh();
    }
    return cache.values();
  }

  @Override
  public V getByCode(C code) {
    if (cache == null) {
      refresh();
    }
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
