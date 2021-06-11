package mp.jprime.dataaccess.transaction;

import mp.jprime.dataaccess.transaction.events.TransactionEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Данные транзации
 */
public final class JPTransactionInfo implements TransactionInfo {
  private final Collection<TransactionEvent> events = new ArrayList<>();
  private final Collection<TransactionEvent> umEvents = Collections.unmodifiableCollection(events);

  @Override
  public void addTransactionEvent(TransactionEvent event) {
    if (event == null) {
      return;
    }
    events.add(event);
  }

  @Override
  public Collection<TransactionEvent> getTransactionEvents() {
    return umEvents;
  }

  /**
   * Конструктор
   *
   * @return Данные транзакции
   */
  public static JPTransactionInfo newInstance() {
    return new JPTransactionInfo();
  }
}
