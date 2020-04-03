package mp.jprime.metamaps.xmlloader.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Arrays;

public class XmlJpAttrMaps {
  @JacksonXmlProperty(localName = "jpAttrMap")
  @JacksonXmlElementWrapper(useWrapping = false)
  private mp.jprime.metamaps.xmlloader.beans.XmlJpAttrMap[] jpAttrMaps;

  public XmlJpAttrMap[] getJpAttrMaps() {
    return jpAttrMaps;
  }

  public void setJpAttrMaps(XmlJpAttrMap[] jpAttrMaps) {
    this.jpAttrMaps = jpAttrMaps;
  }

  @Override
  public String toString() {
    return "XmlJpAttrMaps{" +
        "jpAttrMaps=" + Arrays.toString(jpAttrMaps) +
        '}';
  }
}
