package mp.jprime.events.systemevents.json.services;

import mp.jprime.events.systemevents.JPCommonSystemEvent;
import mp.jprime.events.systemevents.JPSystemEvent;
import mp.jprime.events.systemevents.json.JsonJPSystemEvent;
import org.springframework.stereotype.Service;

/**
 * Конвертация {@link JPSystemEvent} <-> {@link JsonJPSystemEvent}
 */
@Service
public final class JsonJPSystemEventConvertor {

  public JPSystemEvent toJPSystemEvent(JsonJPSystemEvent jsonEvent) {
    if (jsonEvent == null) {
      return null;
    }
    return JPCommonSystemEvent.newBuilder()
        .consumer(jsonEvent.getConsumer())
        .producer(jsonEvent.getProducer())
        .eventDate(jsonEvent.getEventDate())
        .eventCode(jsonEvent.getEventCode())
        .external(jsonEvent.isExternal())
        .data(jsonEvent.getData())
        .build();
  }

  public JsonJPSystemEvent toJsonJPSystemEvent(JPSystemEvent event) {
    if (event == null) {
      return null;
    }
    JsonJPSystemEvent jsonEvent = new JsonJPSystemEvent();
    jsonEvent.setConsumer(event.getConsumer());
    jsonEvent.setProducer(event.getProducer());
    jsonEvent.setEventDate(event.getEventDate());
    jsonEvent.setEventCode(event.getEventCode());
    jsonEvent.setExternal(event.isExternal());
    jsonEvent.setData(event.getData());
    return jsonEvent;
  }
}
