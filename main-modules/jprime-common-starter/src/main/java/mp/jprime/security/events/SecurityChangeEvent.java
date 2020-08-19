package mp.jprime.security.events;

import mp.jprime.events.systemevents.JPCommonSystemEvent;
import mp.jprime.events.systemevents.JPSystemEvent;

/**
 * Событие изменения настроек доступа
 */
public final class SecurityChangeEvent {
  /**
   * Код события  изменения настроек доступа
   */
  public static final String CODE = "securityChangeEvent";

  /**
   * Событие изменения настроек доступа
   */
  public static JPSystemEvent newEvent() {
    return JPCommonSystemEvent.newBuilder()
        .eventCode(CODE)
        .external(true)
        .build();
  }
}
