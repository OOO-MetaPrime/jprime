package mp.jprime.dataaccess.transaction.events.listeners;

import mp.jprime.dataaccess.transaction.events.*;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class JPRunnableTransactionEventListener implements JPTransactionEventListener {
  private final static Collection<String> EVENTS = List.of(
      JPRunnableTransactionEvent.CODE
  );

  @Override
  public Collection<String> getListenEvent() {
    return EVENTS;
  }

  @Override
  public void fireEvents(Collection<JPTransactionEvent> events) {
    for (JPTransactionEvent event : events) {
      if (!JPRunnableTransactionEvent.CODE.equals(event.getCode())) {
        continue;
      }
      if (event instanceof JPRunnableTransactionEvent x) {
        Runnable run = x.getRunnable();
        if (run != null) {
          run.run();
        }
      }
    }
  }
}
