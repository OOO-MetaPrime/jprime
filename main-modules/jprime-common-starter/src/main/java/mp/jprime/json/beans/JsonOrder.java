package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonOrder {
  private String asc;
  private String desc;

  public JsonOrder() {

  }

  public String getAsc() {
    return asc;
  }

  public String getDesc() {
    return desc;
  }

  public JsonOrder asc(String asc) {
    this.asc = asc;
    return this;
  }

  public JsonOrder desc(String desc) {
    this.desc = desc;
    return this;
  }
}
