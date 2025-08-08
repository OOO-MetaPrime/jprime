package mp.jprime.dataaccess.transaction.events;

import java.util.Collection;

/**
 * Обработчик событий транзакций
 */
public interface JPTransactionEventManager {
  /**
   * Обработка событий
   *
   * @param events События
   */
  void fireEvents(Collection<JPTransactionEvent> events);
}
