package mp.jprime.meta.loaders.xml.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import tools.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Arrays;

@JsonRootName(value = "jpClasses")
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
