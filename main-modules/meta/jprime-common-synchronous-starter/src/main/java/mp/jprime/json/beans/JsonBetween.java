package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonBetween {
  private String from;
  private String to;

  public JsonBetween() {

  }

  public JsonBetween(String from, String to) {
    this.from = from;
    this.to = to;
  }

  public String getFrom() {
    return from;
  }

  public String getTo() {
    return to;
  }
}
