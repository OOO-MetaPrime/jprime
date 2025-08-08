package mp.jprime.security.abac.loaders.xml.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "jpAbac")
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
