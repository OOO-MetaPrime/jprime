package mp.jprime.dataaccess.transaction;

import mp.jprime.dataaccess.transaction.events.JPTransactionEvent;

import java.util.Collection;

/**
 * Данные транзакции
 */
public interface JpTransactionInfo {
  /**
   * Добавляет событие транзакции
   *
   * @param event Событие
   */
  void addCommitEvent(JPTransactionEvent event);

  /**
   * Возвращает все события транзакции
   *
   * @return Все события
   */
  Collection<JPTransactionEvent> getCommitEvents();
}
