package mp.jprime.metamaps.xmlloader.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Arrays;

@JacksonXmlRootElement(localName = "jpClassMaps")
public class XmlJpClassMaps {
  @JacksonXmlProperty(localName = "jpClassMap")
  @JacksonXmlElementWrapper(useWrapping = false)
  private XmlJpClassMap[] jpClassMaps;

  public XmlJpClassMap[] getJpClassMaps() {
    return jpClassMaps;
  }

  public void setJpClassMaps(XmlJpClassMap[] jpClassMaps) {
    this.jpClassMaps = jpClassMaps;
  }

  @Override
  public String toString() {
    return "XmlJpClassMaps{" +
        "jpClassMaps=" + Arrays.toString(jpClassMaps) +
        '}';
  }
}
