package mp.jprime.security.loaders.xml.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "jpSecurity")
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
    return "XmlJpClassMaps{" +
        "jpPackages=" + jpPackages +
        '}';
  }
}
