package mp.jprime.events.userevents.services;

import mp.jprime.events.userevents.JPUserEvent;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * Публикация пользовательских событий
 */
public interface UserEventPublisher {
  /**
   * Notify all <strong>matching</strong> listeners registered with this
   * application of an event
   *
   * @param event the event to publish
   */
  void publishEvent(JPUserEvent<?> event);

  /**
   * Notify all <strong>matching</strong> listeners registered with this
   * application of events
   *
   * @param events events to publish
   */
  default void publishEvent(Collection<JPUserEvent<?>> events) {
    if (CollectionUtils.isEmpty(events)) {
      return;
    }

    events.forEach(this::publishEvent);
  }
}
