package mp.jprime.utils.attrvaluechange;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonAttrCodeValue {
  private String attrCode;
  private String attrValue;

  public JsonAttrCodeValue() {
  }

  public String getAttrCode() {
    return attrCode;
  }

  public void setAttrCode(String attrCode) {
    this.attrCode = attrCode;
  }

  public String getAttrValue() {
    return attrValue;
  }

  public void setAttrValue(String attrValue) {
    this.attrValue = attrValue;
  }

  @Override
  public String toString() {
    return "JPAttrCodeValue{" +
        "attrCode='" + attrCode + '\'' +
        ", attrValue='" + attrValue + '\'' +
        '}';
  }
}
