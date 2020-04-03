package mp.jprime.meta.events;

import mp.jprime.events.systemevents.JPSystemEvent;
import mp.jprime.events.systemevents.JPEventInfo;

/**
 * Событие изменения метаописания
 */
public class JPMetaChangeEvent extends JPSystemEvent<JPMetaChangeEvent.Info> {
  /**
   * Изменения метаописания класса
   *
   * @param classCode Код метаописания класса
   */
  public JPMetaChangeEvent(String classCode) {
    this(new Info(classCode));
  }

  /**
   * Изменения метаописания
   */
  public JPMetaChangeEvent() {
    this(new Info(Boolean.TRUE));
  }

  /**
   * Изменения метаописания
   *
   * @param info Данные события
   */
  public JPMetaChangeEvent(Info info) {
    super(info);
  }

  /**
   * Код события
   *
   * @return Код события
   */
  @Override
  public String getEventCode() {
    return "metaChangeEvent";
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
    private String classCode;
    private boolean globalChange;

    private Info() {

    }

    private Info(boolean globalChange) {
      this.globalChange = globalChange;
    }

    private Info(String classCode) {
      this.classCode = classCode;
    }

    /**
     * Кодовое имя измененного класса
     *
     * @return Кодовое имя измененного класса
     */
    public String getClassCode() {
      return classCode;
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
