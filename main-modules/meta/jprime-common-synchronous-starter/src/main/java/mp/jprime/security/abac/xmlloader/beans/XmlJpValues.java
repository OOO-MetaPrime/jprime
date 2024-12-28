package mp.jprime.security.abac.xmlloader.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Arrays;

public class XmlJpValues {
  @JacksonXmlProperty(localName = "value")
  @JacksonXmlElementWrapper(useWrapping = false)
  private String[] value;

  public String[] getValue() {
    return value;
  }

  public void setValue(String[] value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return "XmlJpValues{" +
        "value='" + Arrays.toString(value) + '\'' +
        '}';
  }
}
