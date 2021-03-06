package mp.jprime.security.xmlloader.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Arrays;

public class XmlJpPermitAccess {
  @JacksonXmlProperty(localName = "jpAccess")
  @JacksonXmlElementWrapper(useWrapping = false)
  private XmlJpAccess[] jpAccess;

  public XmlJpAccess[] getJpAccess() {
    return jpAccess;
  }

  public void setJpAccess(XmlJpAccess[] jpAccess) {
    this.jpAccess = jpAccess;
  }

  @Override
  public String toString() {
    return "XmlJpPermitAccess{" +
        "jpAccess=" + Arrays.toString(jpAccess) +
        '}';
  }
}
