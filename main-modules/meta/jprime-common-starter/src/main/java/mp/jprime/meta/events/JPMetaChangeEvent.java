package mp.jprime.meta.events;

import mp.jprime.events.systemevents.JPCommonSystemEvent;
import mp.jprime.events.systemevents.JPSystemEvent;

import java.util.Collections;
import java.util.Map;

/**
 * Событие изменения метаописания
 */
public final class JPMetaChangeEvent {
  /**
   * Код события изменения метаописания
   */
  public static final String CODE = "metaChangeEvent";
  /**
   * Код конструктора меты в параметрах JPSystemEvent
   */
  private static final String EDITOR_CODE = "editorCode";

  private final JPSystemEvent jpSystemEvent;

  private JPMetaChangeEvent(JPSystemEvent jpSystemEvent) {
    this.jpSystemEvent = jpSystemEvent;
  }

  /**
   * Возвращает код конструктора меты
   *
   * @return Код конструктора меты
   */
  public String getStorageCode() {
    Map<String, String> data = jpSystemEvent.getData();
    return data != null ? data.get(EDITOR_CODE) : null;
  }

  /**
   * Событие изменения метаописания
   */
  public static JPMetaChangeEvent from(JPSystemEvent jpSystemEvent) {
    return new JPMetaChangeEvent(jpSystemEvent);
  }

  /**
   * Событие изменения метаописания
   */
  public static JPSystemEvent newEvent(String editorCode) {
    return JPCommonSystemEvent.newBuilder()
        .eventCode(CODE)
        .external(false)
        .data(
            Collections.singletonMap(EDITOR_CODE, editorCode)
        )
        .build();
  }
}
