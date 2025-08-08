package mp.jprime.dataaccess.transaction;

import mp.jprime.dataaccess.transaction.events.JPTransactionEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Данные транзакции
 */
public final class JPTransactionInfo implements TransactionInfo {
  private final Collection<JPTransactionEvent> events = new ArrayList<>();
  private final Collection<JPTransactionEvent> umEvents = Collections.unmodifiableCollection(events);

  @Override
  public void addCommitEvent(JPTransactionEvent event) {
    if (event == null) {
      return;
    }
    events.add(event);
  }

  @Override
  public Collection<JPTransactionEvent> getCommitEvents() {
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
