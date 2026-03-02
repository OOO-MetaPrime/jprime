package mp.jprime.xml.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class XmlParamEnum {
  private String value;
  private String description;
  private String qName;

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getqName() {
    return qName;
  }

  public void setqName(String qName) {
    this.qName = qName;
  }

  @Override
  public String toString() {
    return "XmlParamEnum{" +
        "value='" + value + '\'' +
        ", description='" + description + '\'' +
        ", qName='" + qName + '\'' +
        '}';
  }
}
