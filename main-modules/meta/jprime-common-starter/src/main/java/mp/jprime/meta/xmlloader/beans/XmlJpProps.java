package mp.jprime.meta.xmlloader.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class XmlJpProps {

  @JacksonXmlProperty(localName = "jpProperty")
  @JacksonXmlElementWrapper(useWrapping = false)
  private XmlJpProperty[] jpProperties;

  public XmlJpProperty[] getJpProperties() {
    return jpProperties;
  }

  public void setJpProperties(XmlJpProperty[] jpProperties) {
    this.jpProperties = jpProperties;
  }

  @Override
  public String toString() {
    return "XmlJpProps{" +
        "jpProperties=" + Arrays.toString(jpProperties) +
        '}';
  }
}
