package mp.jprime.globalsettings.events;

import mp.jprime.caches.events.JPCacheRefreshEvent;
import mp.jprime.events.systemevents.JPSystemEvent;

/**
 * Событие изменения настроек системы
 */
public interface JPGlobalSettingsChangeEvent {
  /**
   * Коды кешей
   */
  interface Cache {
    /**
     * Кеш настроек системы
     */
    String CODE = "globalSettingsCache";
  }

  /**
   * Событие изменения
   */
  static JPSystemEvent newEvent() {
    return JPCacheRefreshEvent.newEvent(Cache.CODE);
  }
}
