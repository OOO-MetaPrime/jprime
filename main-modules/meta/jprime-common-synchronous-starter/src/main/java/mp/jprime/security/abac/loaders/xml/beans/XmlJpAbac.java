package mp.jprime.security.abac.loaders.xml.beans;

import com.fasterxml.jackson.annotation.JsonRootName;
import tools.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonRootName(value = "jpAbac")
public class XmlJpAbac {
  @JacksonXmlProperty(localName = "jpPolicySets")
  @JacksonXmlElementWrapper(useWrapping = false)
  private XmlJpPolicySets jpPolicySets;

  public XmlJpPolicySets getJpPolicySets() {
    return jpPolicySets;
  }

  public void setJpPolicySets(XmlJpPolicySets jpPolicySets) {
    this.jpPolicySets = jpPolicySets;
  }

  @Override
  public String toString() {
    return "XmlJpPolicySets{" +
        "jpPolicySets=" + jpPolicySets +
        '}';
  }
}
