package mp.jprime.events.systemevents;

import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

/**
 * Базовый класс событий
 */
public abstract class JPSystemEvent<T extends JPEventInfo> extends ApplicationEvent {
  private LocalDateTime date;
  // Данные события
  private T info;

  /**
   * Create a new JPEvent.
   *
   * @param date Дата события
   * @param info Данные события
   */
  public JPSystemEvent(LocalDateTime date, T info) {
    super("");
    this.date = date;
    this.info = info;
  }

  /**
   * Create a new JPEvent.
   *
   * @param info Данные события
   */
  public JPSystemEvent(T info) {
    super("");
    this.date = LocalDateTime.now();
    this.info = info;
  }

  /**
   * Дата события
   *
   * @return Дата события
   */
  public LocalDateTime getDate() {
    return date;
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
