package mp.jprime.dataaccess.transaction.events;

import java.util.Collection;

/**
 * Слушатель обработчика событий транзакций
 */
public interface JPTransactionEventListener {
  /**
   * Коды обрабатываемых события
   *
   * @return Список кодов событий
   */
  Collection<String> getListenEvent();

  /**
   * Обработка событий
   *
   * @param events События
   */
  void fireEvents(Collection<JPTransactionEvent> events);
}
