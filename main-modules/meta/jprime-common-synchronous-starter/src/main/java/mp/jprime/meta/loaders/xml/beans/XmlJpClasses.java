package mp.jprime.meta.loaders.xml.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Arrays;

@JacksonXmlRootElement(localName = "jpClasses")
@JsonIgnoreProperties(ignoreUnknown = true)
public class XmlJpClasses {
  @JacksonXmlProperty(localName = "jpClass")
  @JacksonXmlElementWrapper(useWrapping = false)
  private XmlJpClass[] jpClasses;

  public XmlJpClass[] getJpClasses() {
    return jpClasses;
  }

  public void setJpClasses(XmlJpClass[] jpClasses) {
    this.jpClasses = jpClasses;
  }

  @Override
  public String toString() {
    return "XmlJpClasses{" +
        "jpClasses=" + Arrays.toString(jpClasses) +
        '}';
  }
}
