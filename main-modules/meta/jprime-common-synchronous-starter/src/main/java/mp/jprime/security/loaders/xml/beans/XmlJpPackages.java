package mp.jprime.security.loaders.xml.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Arrays;

@JacksonXmlRootElement(localName = "jpPackages")
public class XmlJpPackages {
  @JacksonXmlProperty(localName = "jpPackage")
  @JacksonXmlElementWrapper(useWrapping = false)
  private XmlJpPackage[] jpPackages;

  public XmlJpPackage[] getJpPackages() {
    return jpPackages;
  }

  public void setJpPackages(XmlJpPackage[] jpPackages) {
    this.jpPackages = jpPackages;
  }

  @Override
  public String toString() {
    return "XmlJpPackages{" +
        "jpPackages=" + Arrays.toString(jpPackages) +
        '}';
  }
}