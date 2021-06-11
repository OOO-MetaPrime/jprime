package mp.jprime.meta.events;

import org.springframework.context.ApplicationEvent;

/**
 * Событие окончания загрузки метаописания
 */
public class JPMetaLoadFinishEvent extends ApplicationEvent {
  private JPMetaLoadFinishEvent() {
    super("");
  }

  /**
   * Конструктор
   *
   * @return Событие
   */
  public static JPMetaLoadFinishEvent newEvent() {
    return new JPMetaLoadFinishEvent();
  }
}
