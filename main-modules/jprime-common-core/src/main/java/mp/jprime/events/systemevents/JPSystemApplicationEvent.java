package mp.jprime.events.systemevents;

import org.springframework.context.ApplicationEvent;

/**
 * Обертка JPSystemEvent события для ApplicationEvent
 */
public final class JPSystemApplicationEvent extends ApplicationEvent {
  private final JPSystemEvent event;

  public JPSystemApplicationEvent(JPSystemEvent event) {
    super("");
    this.event = event;
  }

  /**
   * Возвращает событие
   *
   * @return Событие
   */
  public JPSystemEvent getEvent() {
    return event;
  }

  /**
   * Возвращает код события
   *
   * @return Код события
   */
  public String getEventCode() {
    return getEvent().getEventCode();
  }

  /**
   * Статический конструтор
   *
   * @param event События
   * @return JPSystemApplicationEvent
   */
  public static JPSystemApplicationEvent from(JPSystemEvent event) {
    return new JPSystemApplicationEvent(event);
  }
}
