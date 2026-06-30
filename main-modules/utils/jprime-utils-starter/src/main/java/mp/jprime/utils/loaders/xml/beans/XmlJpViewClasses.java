package mp.jprime.utils.loaders.xml.beans;

import tools.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.Arrays;

@JsonRootName(value = "jpClasses")
public class XmlJpViewClasses {
  @JacksonXmlProperty(localName = "jpClass")
  @JacksonXmlElementWrapper(useWrapping = false)
  private String[] jpClasses;

  public String[] getJpClasses() {
    return jpClasses;
  }

  public void setJpClasses(String[] jpClasses) {
    this.jpClasses = jpClasses;
  }

  @Override
  public String toString() {
    return "XmlJpViewClasses{" +
        "jpClasses=" + Arrays.toString(jpClasses) +
        '}';
  }
}