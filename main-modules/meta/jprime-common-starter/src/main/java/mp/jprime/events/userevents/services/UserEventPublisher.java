package mp.jprime.events.userevents.services;

import mp.jprime.events.userevents.JPUserEvent;

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
  void publishEvent(JPUserEvent event);
}
