package mp.jprime.globalsettings.json.db.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonDbGlobalValue {
  private Boolean booleanValue;
  private Integer intValue;
  private String stringValue;
  private Collection<Integer> intArrayValue;
  private Collection<String> stringArrayValue;

  public Boolean getBooleanValue() {
    return booleanValue;
  }

  public void setBooleanValue(Boolean booleanValue) {
    this.booleanValue = booleanValue;
  }

  public Integer getIntValue() {
    return intValue;
  }

  public void setIntValue(Integer intValue) {
    this.intValue = intValue;
  }

  public String getStringValue() {
    return stringValue;
  }

  public void setStringValue(String stringValue) {
    this.stringValue = stringValue;
  }

  public Collection<Integer> getIntArrayValue() {
    return intArrayValue;
  }

  public void setIntArrayValue(Collection<Integer> intArrayValue) {
    this.intArrayValue = intArrayValue;
  }

  public Collection<String> getStringArrayValue() {
    return stringArrayValue;
  }

  public void setStringArrayValue(Collection<String> stringArrayValue) {
    this.stringArrayValue = stringArrayValue;
  }
}
