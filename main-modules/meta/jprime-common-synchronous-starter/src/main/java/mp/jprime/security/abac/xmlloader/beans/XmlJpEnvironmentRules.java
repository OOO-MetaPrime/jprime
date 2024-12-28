package mp.jprime.security.abac.xmlloader.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Arrays;

public class XmlJpEnvironmentRules {
  @JacksonXmlProperty(localName = "environmentRule")
  @JacksonXmlElementWrapper(useWrapping = false)
  private XmlJpEnvironmentRule[] environmentRules;

  public XmlJpEnvironmentRule[] getEnvironmentRules() {
    return environmentRules;
  }

  public void setEnvironmentRules(XmlJpEnvironmentRule[] environmentRules) {
    this.environmentRules = environmentRules;
  }

  @Override
  public String toString() {
    return "XmlJpEnvironmentRules{" +
        "environmentRules=" + Arrays.toString(environmentRules) +
        '}';
  }
}
