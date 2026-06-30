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
   * Код события для обновления кэша
   */
  public static final String REFRESH_CODE = "jpCacheRefreshEvent";
  /**
   * Код события после обновления кэша
   */
  public static final String CHANGE_CODE = "jpCacheChangeEvent";
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
   * Событие для обновления кэша
   *
   * @param cacheCode код кэша, требующего обновление
   */
  public static JPSystemEvent newEvent(String cacheCode) {
    return JPCommonSystemEvent.newBuilder()
        .eventCode(REFRESH_CODE)
        .external(false)
        .data(
            Collections.singletonMap(CACHE_CODE, cacheCode)
        )
        .build();
  }

  /**
   * Событие после обновления кэша
   *
   * @param cacheCode код кэша, требующего обновление
   */
  public static JPSystemEvent newChangeEvent(String cacheCode) {
    return JPCommonSystemEvent.newBuilder()
        .eventCode(CHANGE_CODE)
        .external(true)
        .data(
            Collections.singletonMap(CACHE_CODE, cacheCode)
        )
        .build();
  }
}
