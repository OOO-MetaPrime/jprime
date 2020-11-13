package mp.jprime.meta.events;

import mp.jprime.events.systemevents.JPCommonSystemEvent;
import mp.jprime.events.systemevents.JPSystemEvent;

import java.util.HashMap;

/**
 * Событие изменения метаописания
 */
public final class JPMetaChangeEvent {
  /**
   * Код события изменения метаописания
   */
  public static final String CODE = "metaChangeEvent";

  /**
   * Событие изменения метаописания
   */
  public static JPSystemEvent newEvent() {
    return JPCommonSystemEvent.newBuilder()
        .eventCode(CODE)
        .external(true)
        .build();
  }
}
