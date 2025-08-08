package mp.jprime.dataaccess.transaction.events.services;

import mp.jprime.concurrent.JPCompletableFuture;
import mp.jprime.dataaccess.transaction.events.JPTransactionEventListener;
import mp.jprime.dataaccess.transaction.events.JPTransactionEvent;
import mp.jprime.dataaccess.transaction.events.JPTransactionEventManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Обработчик событий транзакций
 */
@Service
public final class JPTransactionEventCommonManager implements JPTransactionEventManager {
  private Map<String, Collection<JPTransactionEventListener>> eventListeners = new HashMap<>();

  /**
   * Считываем слушателей
   */
  @Autowired(required = false)
  private void setHandlers(Collection<JPTransactionEventListener> listeners) {
    if (listeners == null) {
      return;
    }
    Map<String, Collection<JPTransactionEventListener>> eventListeners = new HashMap<>();

    for (JPTransactionEventListener listener : listeners) {
      Collection<String> types = listener.getListenEvent();
      if (types == null || types.isEmpty()) {
        continue;
      }
      for (String type : types) {
        eventListeners.computeIfAbsent(type, k -> new ArrayList<>()).add(listener);
      }
    }
    this.eventListeners = eventListeners;
  }

  @Override
  public void fireEvents(Collection<JPTransactionEvent> events) {
    if (events == null || events.isEmpty()) {
      return;
    }
    JPCompletableFuture.runAsync(() -> subscribeEvents(events));
  }

  /**
   * Обработка событий
   *
   * @param events События
   */
  private void subscribeEvents(Collection<JPTransactionEvent> events) {
    Map<String, Collection<JPTransactionEvent>> eventsByType = new HashMap<>();
    for (JPTransactionEvent event : events) {
      eventsByType.computeIfAbsent(event.getCode(), k -> new ArrayList<>()).add(event);
    }
    eventsByType.forEach((x, y) -> {
      Collection<JPTransactionEventListener> listeners = eventListeners.get(x);
      if (listeners != null) {
        listeners.forEach(listener -> listener.fireEvents(y));
      }
    });
  }
}
