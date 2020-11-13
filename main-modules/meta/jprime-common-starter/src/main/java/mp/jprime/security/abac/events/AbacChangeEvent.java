package mp.jprime.security.abac.events;

import mp.jprime.events.systemevents.JPCommonSystemEvent;
import mp.jprime.events.systemevents.JPSystemEvent;

/**
 * Событие изменения настроек ABAC
 */
public final class AbacChangeEvent {
  /**
   * Код события  изменения настроек ABAC
   */
  public static final String CODE = "abacChangeEvent";

  /**
   * Событие изменения настроек доступа ABAC
   */
  public static JPSystemEvent newEvent() {
    return JPCommonSystemEvent.newBuilder()
        .eventCode(CODE)
        .external(true)
        .build();
  }
}
