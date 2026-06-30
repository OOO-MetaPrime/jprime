package mp.jprime.security.abac.loaders.xml.beans;

import com.fasterxml.jackson.annotation.JsonRootName;
import tools.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Arrays;

@JsonRootName(value = "jpPolicySets")
public class XmlJpPolicySets {
  @JacksonXmlProperty(localName = "jpPolicySet")
  @JacksonXmlElementWrapper(useWrapping = false)
  private XmlJpPolicySet[] jpPolicySet;

  public XmlJpPolicySet[] getJpPolicySet() {
    return jpPolicySet;
  }

  public void setJpPolicySet(XmlJpPolicySet[] jpPolicySet) {
    this.jpPolicySet = jpPolicySet;
  }

  @Override
  public String toString() {
    return "XmlJpPolicySets{" +
        "jpPolicySet=" + Arrays.toString(jpPolicySet) +
        '}';
  }
}