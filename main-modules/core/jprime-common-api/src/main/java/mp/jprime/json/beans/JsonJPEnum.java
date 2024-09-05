package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import mp.jprime.common.JPEnum;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder(
    {
        "value",
        "description",
        "qName"
    }
)
public class JsonJPEnum {
  private Object value;
  private String description;
  private String qName;

  public JsonJPEnum() {
  }

  private JsonJPEnum(Object value, String description, String qName) {
    this.value = value;
    this.description = description;
    this.qName = qName;
  }

  public static JsonJPEnum of(Object value, String description, String qName) {
    return new JsonJPEnum(value, description, qName);
  }

  public static JsonJPEnum of(Object value, String description) {
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

  public static JPEnum toJPEnum(JsonJPEnum json) {
    if (json == null) {
      return null;
    }
    return JPEnum.of(json.getValue(), json.getDescription(), json.getqName());
  }

  public static JsonJPEnum toJson(JPEnum bean) {
    if (bean == null) {
      return null;
    }
    JsonJPEnum result = new JsonJPEnum();
    result.setValue(bean.getValue());
    result.setDescription(bean.getDescription());
    result.setqName(bean.getQName());
    return result;
  }
}
