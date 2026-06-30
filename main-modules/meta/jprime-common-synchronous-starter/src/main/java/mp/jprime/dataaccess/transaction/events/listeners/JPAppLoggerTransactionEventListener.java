package mp.jprime.dataaccess.transaction.events.listeners;

import mp.jprime.dataaccess.Event;
import mp.jprime.dataaccess.transaction.events.JPTransactionJPObjectEvent;
import mp.jprime.dataaccess.transaction.events.*;
import mp.jprime.json.services.QueryService;
import mp.jprime.log.AppLogger;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.security.ConnectionInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

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
  private final AppLogger appLogger;
  // Заполнение запросов на основе JSON
  private final QueryService queryService;
  private final JPMetaStorage metaStorage;

  private JPAppLoggerTransactionEventListener(
      @Autowired AppLogger appLogger,
      @Autowired QueryService queryService,
      @Autowired JPMetaStorage metaStorage
  ) {
    this.appLogger = appLogger;
    this.queryService = queryService;
    this.metaStorage = metaStorage;
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
          json = queryService.toString(((JPUpdateTransactionEvent) event).getQuery(), this::actionLogAttrFilter);
        } else if (Event.CREATE_SUCCESS == x.getEvent()) {
          json = queryService.toString(((JPCreateTransactionEvent) event).getQuery(), this::actionLogAttrFilter);
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

  private Predicate<String> actionLogAttrFilter(String classCode) {
    if (StringUtils.isBlank(classCode)) {
      return null;
    }
    JPClass jpClass = metaStorage.getJPClassByCode(classCode);
    if  (jpClass == null) {
      return null;
    }
    return jpClass::isActionLogAttr;
  }
}
