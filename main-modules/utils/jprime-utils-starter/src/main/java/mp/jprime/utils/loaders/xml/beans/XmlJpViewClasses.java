package mp.jprime.utils.loaders.xml.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Arrays;

@JacksonXmlRootElement(localName = "jpClasses")
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