package mp.jprime.security.abac.xmlloader.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Arrays;

public class XmlJpClasses {
  @JacksonXmlProperty(localName = "jpClass")
  @JacksonXmlElementWrapper(useWrapping = false)
  private String[] jpClass;

  public String[] getJpClass() {
    return jpClass;
  }

  public void setJpClass(String[] jpClass) {
    this.jpClass = jpClass;
  }

  @Override
  public String toString() {
    return "XmlJpClasses{" +
        "jpClass='" + Arrays.toString(jpClass) + '\'' +
        '}';
  }
}
