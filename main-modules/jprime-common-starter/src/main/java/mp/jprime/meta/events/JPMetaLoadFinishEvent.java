package mp.jprime.meta.events;

import org.springframework.context.ApplicationEvent;

/**
 * Событие окончания загрузки метаописания
 */
public class JPMetaLoadFinishEvent extends ApplicationEvent {
  public JPMetaLoadFinishEvent() {
    super("");
  }
}
