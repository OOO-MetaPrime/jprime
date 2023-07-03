package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Модель обновления
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonUpdate extends JsonObjectData {
  @JsonProperty
  private JsonExpr filter;

  public void setFilter(JsonExpr filter) {
    this.filter = filter;
  }

  public JsonExpr getFilter() {
    return filter;
  }

  public JsonUpdate() {
    super();
  }

  public JsonUpdate(Object id, String classCode, Map<String, Object> data) {
    super(id, classCode, data);
  }

  public JsonUpdate(Object id, String classCode, Map<String, Object> data, Map<String, JsonObjectLinkedData> linkedData) {
    super(id, classCode, data, linkedData);
  }
}
