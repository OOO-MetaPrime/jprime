package mp.jprime.caches.services;

import mp.jprime.application.JPApplicationInitListener;
import mp.jprime.caches.JPCache;
import mp.jprime.caches.JPCacheManager;
import mp.jprime.caches.events.JPCacheRefreshEvent;
import mp.jprime.concurrent.JPForkJoinPoolService;
import mp.jprime.events.systemevents.JPSystemApplicationEvent;
import mp.jprime.application.JPApplicationShutdownManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public final class JPCacheManagerService implements JPCacheManager, JPApplicationInitListener {
  private static final Logger LOG = LoggerFactory.getLogger(JPCacheManagerService.class);

  private final JPApplicationShutdownManager shutdownManager;
  private final Map<String, JPCache> caches = new HashMap<>();

  private JPCacheManagerService(@Autowired JPApplicationShutdownManager shutdownManager,
                                @Autowired(required = false) Collection<JPCache> caches) {
    this.shutdownManager = shutdownManager;
    if (caches != null) {
      caches.forEach(x -> this.caches.put(x.getCode(), x));
    }
  }

  @Override
  public void applicationInit() {
    if (caches.isEmpty()) {
      return;
    }
    TreeMap<Integer, Collection<JPCache>> initOrder = new TreeMap<>(Collections.reverseOrder());
    caches.values()
        .forEach(x -> initOrder.computeIfAbsent(x.getOrder(), k -> new ArrayList<>()).add(x));

    for (Collection<JPCache> caches : initOrder.values()) {
      JPForkJoinPoolService.pool().submit(() ->
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
          )
      ).join();
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

  @Override
  public void refresh(String code) {
    JPCache cache = caches.get(code);
    if (cache != null) {
      cache.refresh();
    }
  }
}
