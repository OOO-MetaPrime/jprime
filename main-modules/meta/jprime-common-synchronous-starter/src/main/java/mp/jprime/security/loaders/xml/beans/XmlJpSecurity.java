package mp.jprime.security.loaders.xml.beans;

import com.fasterxml.jackson.annotation.JsonRootName;
import tools.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonRootName(value = "jpSecurity")
public class XmlJpSecurity {
  @JacksonXmlProperty(localName = "jpPackages")
  @JacksonXmlElementWrapper(useWrapping = false)
  private XmlJpPackages jpPackages;

  public XmlJpPackages getJpPackages() {
    return jpPackages;
  }

  public void setJpPackages(XmlJpPackages jpPackages) {
    this.jpPackages = jpPackages;
  }

  @Override
  public String toString() {
    return "XmlJpSecurity{" +
        "jpPackages=" + jpPackages +
        '}';
  }
}
