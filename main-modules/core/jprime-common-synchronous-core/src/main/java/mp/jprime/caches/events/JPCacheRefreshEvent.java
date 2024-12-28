package mp.jprime.caches.events;

import mp.jprime.caches.JPCache;
import mp.jprime.events.systemevents.JPCommonSystemEvent;
import mp.jprime.events.systemevents.JPSystemEvent;

import java.util.Collections;
import java.util.Map;

/**
 * Событие необходимости обновления кэша
 */
public class JPCacheRefreshEvent {
  /**
   * Код события обновления кэша
   */
  public static final String CODE = "jpCacheRefreshEvent";
  /**
   * Код кэша в параметрах JPSystemEvent
   */
  private static final String CACHE_CODE = "cacheCode";

  private final JPSystemEvent jpSystemEvent;

  private JPCacheRefreshEvent(JPSystemEvent jpSystemEvent) {
    this.jpSystemEvent = jpSystemEvent;
  }

  /**
   * Возвращает код кэша
   *
   * @return {@link JPCache#getCode() Код кэша}
   */
  public String getCacheCode() {
    Map<String, String> data = jpSystemEvent.getData();
    return data != null ? data.get(CACHE_CODE) : null;
  }

  /**
   * Событие обновления кэша
   */
  public static JPCacheRefreshEvent from(JPSystemEvent jpSystemEvent) {
    return new JPCacheRefreshEvent(jpSystemEvent);
  }

  /**
   * Событие обновления кэша
   *
   * @param cacheCode код кэша, требующего обновление
   */
  public static JPSystemEvent newEvent(String cacheCode) {
    return JPCommonSystemEvent.newBuilder()
        .eventCode(CODE)
        .external(false)
        .data(
            Collections.singletonMap(CACHE_CODE, cacheCode)
        )
        .build();
  }
}
