package mp.jprime.caches.services;

import mp.jprime.caches.JPCache;
import mp.jprime.caches.JPCacheManager;
import mp.jprime.caches.events.JPCacheRefreshEvent;
import mp.jprime.events.systemevents.JPSystemApplicationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class JPCacheManagerService implements JPCacheManager {
  private static final Logger LOG = LoggerFactory.getLogger(JPCacheManagerService.class);

  private final Map<String, JPCache<?, ?>> caches = new HashMap<>();

  @Autowired(required = false)
  public void setCaches(Collection<JPCache<?, ?>> caches) {
    if (caches != null) {
      caches.forEach(cache -> this.caches.put(cache.getCode(), cache));
    }
  }

  @EventListener
  private void listenContextRefresh(ContextRefreshedEvent event) {
    caches.values().forEach(cache -> CompletableFuture.runAsync(() -> {
              try {
                cache.refresh();
              } catch (Exception e) {
                LOG.error("Error during refreshing cache {}, cause: {}", cache.getCode(), e.getMessage(), e);
              }
            }
        )
    );
  }

  /**
   * Код события обновления кэша
   */
  public String getRefreshEventCode() {
    return JPCacheRefreshEvent.CODE;
  }

  @EventListener(condition = "#event.eventCode.equals(@JPCacheManagerService.getRefreshEventCode())")
  private void listenCacheRefresh(JPSystemApplicationEvent event) {
    JPCacheRefreshEvent cacheRefreshEvent = JPCacheRefreshEvent.from(event.getEvent());
    refresh(cacheRefreshEvent.getCacheCode());
  }

  /**
   * Обновить кэш по коду
   *
   * @param code код кэша
   */
  @Override
  public void refresh(String code) {
    JPCache<?, ?> cache = caches.get(code);
    if (cache != null) {
      cache.refresh();
    }
  }
}
