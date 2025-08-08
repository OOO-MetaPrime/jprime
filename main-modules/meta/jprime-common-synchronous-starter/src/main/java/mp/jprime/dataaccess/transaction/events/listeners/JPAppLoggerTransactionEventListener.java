package mp.jprime.dataaccess.transaction.events.listeners;

import mp.jprime.dataaccess.Event;
import mp.jprime.dataaccess.transaction.events.JPTransactionJPObjectEvent;
import mp.jprime.dataaccess.transaction.events.*;
import mp.jprime.json.services.QueryService;
import mp.jprime.log.AppLogger;
import mp.jprime.security.ConnectionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

@Service
public class JPAppLoggerTransactionEventListener implements JPTransactionEventListener {
  private final static Collection<String> EVENTS = Set.of(
      JPCreateTransactionEvent.CODE,
      JPDeleteTransactionEvent.CODE,
      JPUpdateTransactionEvent.CODE
  );

  @Override
  public Collection<String> getListenEvent() {
    return EVENTS;
  }

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
  public void fireEvents(Collection<JPTransactionEvent> events) {
    for (JPTransactionEvent event : events) {
      if (event instanceof JPTransactionJPObjectEvent x) {
        String json;
        if (Event.UPDATE_SUCCESS == x.getEvent()) {
          json = queryService.toString(((JPUpdateTransactionEvent) event).getQuery());
        } else if (Event.CREATE_SUCCESS == x.getEvent()) {
          json = queryService.toString(((JPCreateTransactionEvent) event).getQuery());
        } else if (Event.DELETE_SUCCESS == x.getEvent()) {
          json = queryService.toString(((JPDeleteTransactionEvent) event).getQuery());
        } else {
          continue;
        }

        ConnectionInfo connInfo = x.getConnInfo();
        String subject = connInfo != null ? connInfo.getUsername() : null;
        appLogger.debug(
            x.getEvent(), subject,
            String.valueOf(x.getId()), x.getJpClassCode(),
            json, connInfo
        );
      }
    }
  }
}
