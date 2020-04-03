package mp.jprime.security.events;

import mp.jprime.events.systemevents.JPEventInfo;
import mp.jprime.events.systemevents.JPSystemEvent;

/**
 * Событие изменения настроек доступа
 */
public class SecurityChangeEvent extends JPSystemEvent<SecurityChangeEvent.Info> {
  /**
   * Изменения настроек доступа
   *
   * @param code Код настроек доступа
   */
  public SecurityChangeEvent(String code) {
    this(new Info(code));
  }

  /**
   * Изменения настроек доступа
   */
  public SecurityChangeEvent() {
    this(new Info(Boolean.TRUE));
  }

  /**
   * Изменения настроек доступа
   *
   * @param info Данные события
   */
  public SecurityChangeEvent(Info info) {
    super(info);
  }

  /**
   * Код события
   *
   * @return Код события
   */
  @Override
  public String getEventCode() {
    return "securityChangeEvent";
  }

  /**
   * Признак внешнего события (можно пересылать за пределы системы, в UI например)
   *
   * @return Да/Нет
   */
  @Override
  public boolean isExternal() {
    return true;
  }

  /**
   * Описание события
   */
  public static class Info implements JPEventInfo {
    private String code;
    private boolean globalChange;

    private Info() {

    }

    private Info(boolean globalChange) {
      this.globalChange = globalChange;
    }

    private Info(String code) {
      this.code = code;
    }

    /**
     * Кодовое имя настроек доступа
     *
     * @return Кодовое имя настроек доступа
     */
    public String getCode() {
      return code;
    }

    /**
     * Признак глобального изменения
     *
     * @return Признак глобального изменения
     */
    public boolean isGlobalChange() {
      return globalChange;
    }
  }
}
