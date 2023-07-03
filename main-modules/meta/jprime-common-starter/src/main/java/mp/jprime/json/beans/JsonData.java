package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Модель списка данных
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonData {
  private Map<String, Object> data = new HashMap<>();

  public JsonData() {
    super();
  }

  public JsonData(Map<String, Object> data) {
    this.data = data;
  }

  public Map<String, Object> getData() {
    return data;
  }

  public void setData(Map<String, Object> data) {
    this.data = data;
  }
}