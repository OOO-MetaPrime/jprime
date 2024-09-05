package mp.jprime.json.db.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import mp.jprime.common.JPEnum;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonDbJPEnum {
  private Object value;
  private String description;
  private String qName;

  public JsonDbJPEnum() {
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getqName() {
    return qName;
  }

  public void setqName(String qName) {
    this.qName = qName;
  }

  public static JPEnum toJPEnum(JsonDbJPEnum json) {
    if (json == null) {
      return null;
    }
    return JPEnum.of(json.getValue(), json.getDescription(), json.getqName());
  }

  public static JsonDbJPEnum toJson(JPEnum bean) {
    if (bean == null) {
      return null;
    }
    JsonDbJPEnum result = new JsonDbJPEnum();
    result.setValue(bean.getValue());
    result.setDescription(bean.getDescription());
    result.setqName(bean.getQName());
    return result;
  }
}
