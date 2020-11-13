package mp.jprime.security.abac.xmlloader.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Arrays;

@JacksonXmlRootElement(localName = "jpPolicySets")
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