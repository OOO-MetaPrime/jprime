package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

/*
 * Модель данных ответа со значениями по умолчанию
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonDefValueResult {
  private String classCode;
  private Map<String, Object> data;

  public JsonDefValueResult() {

  }

  private JsonDefValueResult(String classCode, Map<String, Object> data) {
    this.classCode = classCode;
    this.data = data;
  }

  public static JsonDefValueResult of(String classCode, Map<String, Object> data) {
    return new JsonDefValueResult(classCode, data);
  }

  public String getClassCode() {
    return classCode;
  }

  public Map<String, Object> getData() {
    return data;
  }
}
