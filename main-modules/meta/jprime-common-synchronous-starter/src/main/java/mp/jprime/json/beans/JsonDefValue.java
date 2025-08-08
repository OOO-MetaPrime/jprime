package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mp.jprime.lang.JPMap;

import java.util.Map;

/*
 * Модель данных ответа со значениями по умолчанию
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonDefValue {
  private String classCode;
  private Map<String, Object> data;

  public JsonDefValue() {

  }

  private JsonDefValue(String classCode, Map<String, Object> data) {
    this.classCode = classCode;
    this.data = data;
  }

  public static JsonDefValue of(String classCode, JPMap data) {
    return new JsonDefValue(classCode, data != null ? data.toMap() : null);
  }

  public static JsonDefValue of(String classCode, Map<String, Object> data) {
    return new JsonDefValue(classCode, data);
  }

  public String getClassCode() {
    return classCode;
  }

  public Map<String, Object> getData() {
    return data;
  }
}
