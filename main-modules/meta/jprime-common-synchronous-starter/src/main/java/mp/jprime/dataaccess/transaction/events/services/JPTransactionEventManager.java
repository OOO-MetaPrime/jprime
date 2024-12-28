package mp.jprime.dataaccess.transaction.events.services;

import mp.jprime.annotations.JPClassesLink;
import mp.jprime.common.JPClassesLinkFilter;
import mp.jprime.dataaccess.transaction.events.TransactionEventManager;
import mp.jprime.dataaccess.transaction.listeners.TransactionEventListener;
import mp.jprime.dataaccess.transaction.events.TransactionEvent;
import mp.jprime.exceptions.JPRuntimeException;
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
public final class JPTransactionEventManager implements TransactionEventManager, JPClassesLinkFilter<TransactionEventListener> {
  private Map<String, Collection<TransactionEventListener>> jpClassListeners = new HashMap<>();
  private Collection<TransactionEventListener> uniListeners = new ArrayList<>();

  /**
   * Считываем слушателей
   */
  @Autowired(required = false)
  private void setHandlers(Collection<TransactionEventListener> listeners) {
    if (listeners == null) {
      return;
    }
    Map<String, Collection<TransactionEventListener>> jpClassListeners = new HashMap<>();
    Collection<TransactionEventListener> uniListeners = new ArrayList<>();
    for (TransactionEventListener listener : listeners) {
      try {
        JPClassesLink anno = listener.getClass().getAnnotation(JPClassesLink.class);
        if (anno == null) {
          continue;
        }
        if (anno.uni()) {
          uniListeners.add(listener);
        } else {
          for (String jpClassCode : anno.jpClasses()) {
            if (jpClassCode == null || jpClassCode.isEmpty()) {
              continue;
            }
            jpClassListeners.computeIfAbsent(jpClassCode, x -> new ArrayList<>()).add(listener);
          }
        }
      } catch (Exception e) {
        throw JPRuntimeException.wrapException(e);
      }
    }
    uniListeners = filter(uniListeners);

    Collection<String> keys = jpClassListeners.keySet();
    for (String key : keys) {
      Collection<TransactionEventListener> values = jpClassListeners.get(key);
      jpClassListeners.put(key, filter(values));
    }

    this.uniListeners = uniListeners;
    this.jpClassListeners = jpClassListeners;
  }


  /**
   * Обработка событий
   *
   * @param events События
   */
  @Override
  public void fireEvents(Collection<TransactionEvent> events) {
    if (events == null || events.isEmpty()) {
      return;
    }
    subscribeEvents(events);
  }

  /**
   * Обработка событий
   *
   * @param events События
   */
  private void subscribeEvents(Collection<TransactionEvent> events) {
    if (!jpClassListeners.isEmpty()) {
      Map<String, Collection<TransactionEvent>> jpClassEvents = new HashMap<>();
      for (TransactionEvent event : events) {
        jpClassEvents.computeIfAbsent(event.getJpClassCode(), x -> new ArrayList<>()).add(event);
      }
      jpClassEvents.forEach((x, y) -> {
        Collection<TransactionEventListener> listeners = jpClassListeners.get(x);
        if (listeners != null) {
          listeners.forEach(listener -> listener.fireEvents(y));
        }
      });
    }
    uniListeners.forEach(listener -> listener.fireEvents(events));
  }
}
