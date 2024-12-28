package mp.jprime.dataaccess.transaction;

import mp.jprime.dataaccess.transaction.events.TransactionEvent;

import java.util.Collection;

/**
 * Данные транзакции
 */
public interface TransactionInfo {
  /**
   * Добавляет событие транзакции
   *
   * @param event Событие
   */
  void addTransactionEvent(TransactionEvent event);

  /**
   * Возвращает все события транзакции
   *
   * @return Все события
   */
  Collection<TransactionEvent> getTransactionEvents();
}
