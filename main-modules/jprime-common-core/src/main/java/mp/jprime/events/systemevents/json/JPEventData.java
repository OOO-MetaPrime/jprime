package mp.jprime.events.systemevents.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JPEventData {
  private LocalDateTime date;
  private String eventClassName;
  private String infoClassName;
  private String info;

  @JsonCreator
  public JPEventData(@JsonProperty("date") LocalDateTime date,
                     @JsonProperty("eventClassName") String eventClassName,
                     @JsonProperty("infoClassName") String infoClassName,
                     @JsonProperty("info") String info) {
    this.date = date;
    this.eventClassName = eventClassName;
    this.infoClassName = infoClassName;
    this.info = info;
  }

  public LocalDateTime getDate() {
    return date;
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
