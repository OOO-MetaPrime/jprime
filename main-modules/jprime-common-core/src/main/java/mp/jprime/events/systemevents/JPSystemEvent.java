package mp.jprime.events.systemevents;

import org.springframework.context.ApplicationEvent;

/**
 * Базовый класс событий
 */
public abstract class JPSystemEvent<T extends JPEventInfo> extends ApplicationEvent {
  // Данные события
  private T info;

  /**
   * Create a new JPEvent.
   *
   * @param info Данные события
   */
  public JPSystemEvent(T info) {
    super("");
    this.info = info;
  }

  /**
   * Данные события
   *
   * @return Данные события
   */
  public T getInfo() {
    return info;
  }

  /**
   * Код события
   *
   * @return Код события
   */
  abstract public String getEventCode();

  /**
   * Признак внешнего события (можно пересылать за пределы системы, в UI например)
   *
   * @return Да/Нет
   */
  public boolean isExternal() {
    return false;
  }
}
