package mp.jprime.events.systemevents.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonJPSystemEvent {
  private String producer;
  private String consumer;
  private LocalDateTime eventDate;
  private String eventCode;
  private boolean external;
  private Map<String, String> data = new HashMap<>();

  public String getProducer() {
    return producer;
  }

  public void setProducer(String producer) {
    this.producer = producer;
  }

  public String getConsumer() {
    return consumer;
  }

  public void setConsumer(String consumer) {
    this.consumer = consumer;
  }

  public LocalDateTime getEventDate() {
    return eventDate;
  }

  public void setEventDate(LocalDateTime eventDate) {
    this.eventDate = eventDate;
  }

  public String getEventCode() {
    return eventCode;
  }

  public void setEventCode(String eventCode) {
    this.eventCode = eventCode;
  }

  public boolean isExternal() {
    return external;
  }

  public void setExternal(boolean external) {
    this.external = external;
  }

  public Map<String, String> getData() {
    return data;
  }

  public void setData(Map<String, String> data) {
    this.data = data;
  }
}
