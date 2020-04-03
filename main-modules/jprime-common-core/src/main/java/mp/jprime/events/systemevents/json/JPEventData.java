package mp.jprime.events.systemevents.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JPEventData {
  private String eventClassName;
  private String infoClassName;
  private String info;

  @JsonCreator
  public JPEventData(@JsonProperty("eventClassName") String eventClassName,
                     @JsonProperty("infoClassName") String infoClassName,
                     @JsonProperty("info") String info) {
    this.eventClassName = eventClassName;
    this.infoClassName = infoClassName;
    this.info = info;
  }

  public String getEventClassName() {
    return eventClassName;
  }

  public String getInfoClassName() {
    return infoClassName;
  }

  public String getInfo() {
    return info;
  }
}
