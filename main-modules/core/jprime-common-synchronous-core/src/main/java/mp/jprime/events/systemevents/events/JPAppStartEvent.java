package mp.jprime.events.systemevents.events;

import mp.jprime.events.systemevents.JPCommonSystemEvent;
import mp.jprime.events.systemevents.JPSystemEvent;

import java.util.HashMap;

/**
 * Событие запуска системы
 */
public final class JPAppStartEvent {
  /**
   * Код события запуска системы
   */
  public static final String CODE = "uiChangeEvent";
  /**
   * Событие запуска системы
   *
   * @param appCode    Код сервиса
   * @param isExternal Признак внешнего события (можно пересылать за пределы системы, в UI например)
   */
  public static JPSystemEvent newEvent(String appCode, boolean isExternal) {
    return JPCommonSystemEvent.newBuilder()
        .eventCode(CODE)
        .external(isExternal)
        .data(new HashMap<>() {
          {
            put("appCode", appCode);
          }
        })
        .build();
  }
}
