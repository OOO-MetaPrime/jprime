package mp.jprime.security.abac.xmlloader.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Arrays;

public class XmlJpResourceRules {
  @JacksonXmlProperty(localName = "resourceRule")
  @JacksonXmlElementWrapper(useWrapping = false)
  private XmlJpResourceRule[] resourceRules;

  public XmlJpResourceRule[] getResourceRules() {
    return resourceRules;
  }

  public void setResourceRules(XmlJpResourceRule[] resourceRules) {
    this.resourceRules = resourceRules;
  }

  @Override
  public String toString() {
    return "XmlJpResourceRules{" +
        "resourceRules=" + Arrays.toString(resourceRules) +
        '}';
  }
}
