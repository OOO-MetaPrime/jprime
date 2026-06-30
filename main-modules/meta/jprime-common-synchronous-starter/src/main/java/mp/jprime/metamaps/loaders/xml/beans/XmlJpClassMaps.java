package mp.jprime.metamaps.loaders.xml.beans;

import com.fasterxml.jackson.annotation.JsonRootName;
import tools.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Arrays;

@JsonRootName(value = "jpClassMaps")
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
