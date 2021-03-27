package mp.jprime.dataaccess.transaction.listeners;

import mp.jprime.annotations.JPClassesLink;
import mp.jprime.dataaccess.Event;
import mp.jprime.dataaccess.transaction.events.JPCreateTransactionEvent;
import mp.jprime.dataaccess.transaction.events.JPDeleteTransactionEvent;
import mp.jprime.dataaccess.transaction.events.JPUpdateTransactionEvent;
import mp.jprime.dataaccess.transaction.events.TransactionEvent;
import mp.jprime.json.services.QueryService;
import mp.jprime.log.AppLogger;
import mp.jprime.security.ConnectionInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

@JPClassesLink(
    uni = true
)
public class JPAppLoggerTransactionEventListener implements TransactionEventListener {
  // Системный журнал
  private AppLogger appLogger;
  // Заполнение запросов на основе JSON
  private QueryService queryService;

  @Autowired
  private void setAppLogger(AppLogger appLogger) {
    this.appLogger = appLogger;
  }

  @Autowired
  private void setQueryService(QueryService queryService) {
    this.queryService = queryService;
  }

  /**
   * Обработка событий
   *
   * @param events События
   */
  @Override
  public void fireEvents(Collection<TransactionEvent> events) {
    for (TransactionEvent event : events) {
      ConnectionInfo connInfo = event.getConnInfo();
      String subject = connInfo != null ? connInfo.getUsername() : null;
      appLogger.debug(
          event.getEvent(), subject,
          String.valueOf(event.getId()), event.getJpClassCode(),
          toJson(event), connInfo
      );
    }
  }

  private String toJson(TransactionEvent event) {
    if (Event.UPDATE_SUCCESS == event.getEvent()) {
      return queryService.toString(((JPUpdateTransactionEvent) event).getQuery());
    } else if (Event.CREATE_SUCCESS == event.getEvent()) {
      return queryService.toString(((JPCreateTransactionEvent) event).getQuery());
    } else if (Event.DELETE_SUCCESS == event.getEvent()) {
      return queryService.toString(((JPDeleteTransactionEvent) event).getQuery());
    }
    return null;
  }
}
