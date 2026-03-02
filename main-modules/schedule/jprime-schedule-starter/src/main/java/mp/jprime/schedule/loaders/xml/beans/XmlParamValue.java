package mp.jprime.schedule.loaders.xml.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "paramValue")
@JsonIgnoreProperties(ignoreUnknown = true)
public class XmlParamValue {
  private String code;
  private String value;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return "XmlJpDefValuesParam{" +
        ",code=" + code +
        ",value=" + value +
        '}';
  }
}
