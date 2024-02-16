package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonEnum {
  private Object value;
  private String description;
  private String qName;

  public JsonEnum() {
  }

  private JsonEnum(Object value, String description, String qName) {
    this.value = value;
    this.description = description;
    this.qName = qName;
  }

  public static JsonEnum of(Object value, String description, String qName) {
    return new JsonEnum(value, description, qName);
  }

  public static JsonEnum of(Object value, String description) {
    return of(value, description, null);
  }

  public Object getValue() {
    return value;
  }

  public String getDescription() {
    return description;
  }

  public String getqName() {
    return qName;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setqName(String qName) {
    this.qName = qName;
  }
}
