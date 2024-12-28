package mp.jprime.events.systemevents.services;

import mp.jprime.events.systemevents.JPSystemEvent;

/**
 * Публикация глобальных системных событий
 */
public interface GlobalEventPublisher {
  /**
   * Notify all <strong>matching</strong> listeners registered with this
   * application of an event
   *
   * @param event the event to publish
   */
  void publishEvent(JPSystemEvent event);

}
