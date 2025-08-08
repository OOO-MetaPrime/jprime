package mp.jprime.security.abac.loaders.xml.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Arrays;

public class XmlJpPolicies {
  @JacksonXmlProperty(localName = "jpPolicy")
  @JacksonXmlElementWrapper(useWrapping = false)
  private  XmlJpPolicy[] jpPolicy;

  public XmlJpPolicy[] getJpPolicy() {
    return jpPolicy;
  }

  public void setJpPolicy(XmlJpPolicy[] jpPolicy) {
    this.jpPolicy = jpPolicy;
  }

  @Override
  public String toString() {
    return "XmlJpPolicies{" +
        "jpPolicy='" + Arrays.toString(jpPolicy) + '\'' +
        '}';
  }
}
