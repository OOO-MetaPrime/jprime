package mp.jprime.dataaccess.transaction.listeners;

import mp.jprime.dataaccess.Event;
import mp.jprime.dataaccess.transaction.events.JPCreateTransactionEvent;
import mp.jprime.dataaccess.transaction.events.JPDeleteTransactionEvent;
import mp.jprime.dataaccess.transaction.events.JPUpdateTransactionEvent;
import mp.jprime.dataaccess.transaction.events.TransactionEvent;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Слушатель обработчика событий транзакций
 */
public interface TransactionEventListener {

  /**
   * Обработка событий
   *
   * @param events События
   */
  default void fireEvents(Collection<TransactionEvent> events) {
    Collection<JPCreateTransactionEvent> createEvents = new ArrayList<>();
    Collection<JPUpdateTransactionEvent> updateEvents = new ArrayList<>();
    Collection<JPDeleteTransactionEvent> deleteEvents = new ArrayList<>();
    events.forEach(event -> {
          if (Event.UPDATE_SUCCESS == event.getEvent()) {
            updateEvents.add((JPUpdateTransactionEvent) event);
          } else if (Event.CREATE_SUCCESS == event.getEvent()) {
            createEvents.add((JPCreateTransactionEvent) event);
          } else if (Event.DELETE_SUCCESS == event.getEvent()) {
            deleteEvents.add((JPDeleteTransactionEvent) event);
          }
        }
    );
    fireEvents(
        createEvents, updateEvents, deleteEvents
    );
  }

  /**
   * Обработка событий
   *
   * @param createEvents События создания
   * @param updateEvents События обновления
   * @param deleteEvents События удаления
   */
  default void fireEvents(
      Collection<JPCreateTransactionEvent> createEvents,
      Collection<JPUpdateTransactionEvent> updateEvents,
      Collection<JPDeleteTransactionEvent> deleteEvents
  ) {

  }
}
