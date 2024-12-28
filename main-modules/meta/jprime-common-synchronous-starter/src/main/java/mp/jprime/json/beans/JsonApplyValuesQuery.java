package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Модель запроса на пополнение значений
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonApplyValuesQuery {
  private Object id = null;
  private String classCode = null;
  private Collection<String> attrs = new ArrayList<>();
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

  public Collection<String> getAttrs() {
    return attrs;
  }

  public void setAttrs(Collection<String> attrs) {
    this.attrs = attrs;
  }

  public Map<String, Object> getData() {
    return data;
  }

  public void setData(Map<String, Object> data) {
    this.data = data;
  }
}
