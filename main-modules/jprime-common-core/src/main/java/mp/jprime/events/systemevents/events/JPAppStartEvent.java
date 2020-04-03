package mp.jprime.events.systemevents.events;

import mp.jprime.events.systemevents.JPEventInfo;
import mp.jprime.events.systemevents.JPSystemEvent;

/**
 * Событие запуска системы
 */
public class JPAppStartEvent extends JPSystemEvent<JPAppStartEvent.Info> {
  /**
   * Событие запуска системы
   *
   * @param appCode    Код сервиса
   * @param eventCode  Код события
   * @param isExternal Признак внешнего события (можно пересылать за пределы системы, в UI например)
   */
  public JPAppStartEvent(String appCode, String eventCode, boolean isExternal) {
    this(new Info(appCode, eventCode, isExternal));
  }

  /**
   * Событие запуска системы
   */
  public JPAppStartEvent() {
    this(new Info(null, null, Boolean.FALSE));
  }

  /**
   * Событие запуска системы
   *
   * @param info Данные события
   */
  public JPAppStartEvent(Info info) {
    super(info);
  }

  /**
   * Код события
   *
   * @return Код события
   */
  @Override
  public String getEventCode() {
    return "jpAppStartEvent";
  }

  /**
   * Код сервиса
   *
   * @return Код сервиса
   */
  public String getAppCode() {
    Info info = getInfo();
    return info != null ? info.getAppCode() : null;
  }

  /**
   * Код события сервиса
   *
   * @return Код события сервиса
   */
  public String getAppEventCode() {
    Info info = getInfo();
    return info != null ? info.getEventCode() : null;
  }

  /**
   * Признак внешнего события (можно пересылать за пределы системы, в UI например)
   *
   * @return Да/Нет
   */
  @Override
  public boolean isExternal() {
    Info info = getInfo();
    return info != null && info.isExternal();
  }

  /**
   * Описание события
   */
  public static class Info implements JPEventInfo {
    private String appCode;
    private String eventCode;
    private boolean external;

    private Info() {

    }

    private Info(String appCode, String eventCode, boolean external) {
      this.appCode = appCode;
      this.eventCode = eventCode;
      this.external = external;
    }

    /**
     * Код сервиса
     *
     * @return Код сервиса
     */
    public String getAppCode() {
      return appCode;
    }

    /**
     * Код события
     *
     * @return Код события
     */
    public String getEventCode() {
      return eventCode;
    }

    /**
     * Признак внешнего события (можно пересылать за пределы системы, в UI например)
     *
     * @return Признак внешнего события (можно пересылать за пределы системы, в UI например)
     */
    public boolean isExternal() {
      return external;
    }
  }
}
