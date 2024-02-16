package mp.jprime.caches.services;

import mp.jprime.caches.JPCache;
import mp.jprime.caches.JPCacheManager;
import mp.jprime.caches.events.JPCacheRefreshEvent;
import mp.jprime.events.systemevents.JPSystemApplicationEvent;
import mp.jprime.util.JPApplicationShutdownManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JPCacheManagerService implements JPCacheManager {
  private static final Logger LOG = LoggerFactory.getLogger(JPCacheManagerService.class);

  private final Map<String, JPCache<?, ?>> caches = new HashMap<>();
  private JPApplicationShutdownManager shutdownManager;

  @Autowired(required = false)
  public void setCaches(Collection<JPCache<?, ?>> caches) {
    if (caches != null) {
      caches.forEach(cache -> this.caches.put(cache.getCode(), cache));
    }
  }

  @Autowired
  private void setShutdownManager(JPApplicationShutdownManager shutdownManager) {
    this.shutdownManager = shutdownManager;
  }

  @EventListener
  private void listenContextRefresh(ContextRefreshedEvent event) {
    TreeMap<Integer, Collection<JPCache<?, ?>>> initOrder = new TreeMap<>(Collections.reverseOrder());
    caches.values()
        .forEach(x -> initOrder.computeIfAbsent(x.getOrder(), k -> new ArrayList<>()).add(x));

    for (Collection<JPCache<?, ?>> caches : initOrder.values()) {
      caches.parallelStream().forEach(cache -> {
            try {
              cache.refresh();
            } catch (Exception e) {
              LOG.error("Error during refreshing cache {}, cause: {}", cache.getCode(), e.getMessage(), e);
              if (cache.isFailFastOnStartUp()) {
                shutdownManager.exitWithError();
              }
            }
          }
      );
    }
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
