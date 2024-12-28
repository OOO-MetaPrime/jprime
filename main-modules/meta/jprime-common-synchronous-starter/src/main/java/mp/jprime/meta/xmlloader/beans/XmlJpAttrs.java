package mp.jprime.meta.xmlloader.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class XmlJpAttrs {
  @JacksonXmlProperty(localName = "jpAttr")
  @JacksonXmlElementWrapper(useWrapping = false)
  private XmlJpAttr[] jpAttrs;

  public XmlJpAttr[] getJpAttrs() {
    return jpAttrs;
  }

  public void setJpAttrs(XmlJpAttr[] jpAttrs) {
    this.jpAttrs = jpAttrs;
  }

  @Override
  public String toString() {
    return "XmlJpAttrs{" +
        "jpAttrs=" + Arrays.toString(jpAttrs) +
        '}';
  }
}
