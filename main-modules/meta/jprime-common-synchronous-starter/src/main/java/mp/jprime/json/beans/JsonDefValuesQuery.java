package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Модель запрос на значения по умолчанию
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonDefValuesQuery {
  private Object id = null;
  private String classCode = null;
  private String refAttrCode = null;
  private Map<String, Object> data = new HashMap<>();

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

  public String getRefAttrCode() {
    return refAttrCode;
  }

  public void setRefAttrCode(String refAttrCode) {
    this.refAttrCode = refAttrCode;
  }

  public Map<String, Object> getData() {
    return data;
  }

  public void setData(Map<String, Object> data) {
    this.data = data;
  }
}
