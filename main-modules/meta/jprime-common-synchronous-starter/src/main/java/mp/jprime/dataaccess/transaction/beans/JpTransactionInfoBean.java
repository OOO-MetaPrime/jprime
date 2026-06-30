package mp.jprime.dataaccess.transaction.beans;

import mp.jprime.dataaccess.transaction.JpTransactionInfo;
import mp.jprime.dataaccess.transaction.events.JPTransactionEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Данные транзакции
 */
public final class JpTransactionInfoBean implements JpTransactionInfo {
  private final Collection<JPTransactionEvent> events = new ArrayList<>();
  private final Collection<JPTransactionEvent> umEvents = Collections.unmodifiableCollection(events);

  private JpTransactionInfoBean() {
  }

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
  public static JpTransactionInfoBean newInstance() {
    return new JpTransactionInfoBean();
  }
}
