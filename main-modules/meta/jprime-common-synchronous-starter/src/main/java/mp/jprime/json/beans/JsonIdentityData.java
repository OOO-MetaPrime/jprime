package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Модель идентификатора объекта
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonIdentityData {
  private Object id = null;
  private String classCode = null;

  public Object getId() {
    return id;
  }

  public void setId(Object id) {
    this.id = id;
  }

  public String getClassCode() {
    return classCode;
  }

  public void setClassCode(String classCode) {
    this.classCode = classCode;
  }

  public JsonIdentityData() {

  }

  public JsonIdentityData(Object id, String classCode) {
    this.id = id;
    this.classCode = classCode;
  }
}
