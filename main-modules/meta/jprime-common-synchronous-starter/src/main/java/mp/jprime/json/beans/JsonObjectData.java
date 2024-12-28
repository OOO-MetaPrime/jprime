package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Модель объекта
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonObjectData extends JsonIdentityData {
  private Map<String, Object> data = new HashMap<>();
  private Map<String, JsonObjectLinkedData> linkedData;


  public Map<String, Object> getData() {
    return data;
  }

  public void setData(Map<String, Object> data) {
    this.data = data;
  }

  public Map<String, JsonObjectLinkedData> getLinkedData() {
    return linkedData;
  }

  public void setLinkedData(Map<String, JsonObjectLinkedData> linkedData) {
    this.linkedData = linkedData;
  }

  public JsonObjectData() {
    super();
  }

  public JsonObjectData(Object id, String classCode, Map<String, Object> data) {
    super(id, classCode);
    this.data = data;
  }

  public JsonObjectData(Object id, String classCode, Map<String, Object> data, Map<String, JsonObjectLinkedData> linkedData) {
    super(id, classCode);
    this.data = data;
    this.linkedData = linkedData;
  }
}
