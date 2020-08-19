package mp.jprime.security.abac.xmlloader.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Arrays;

public class XmlJpEnviromentRules {
  @JacksonXmlProperty(localName = "enviromentRule")
  @JacksonXmlElementWrapper(useWrapping = false)
  private XmlJpEnviromentRule[] enviromentRules;

  public XmlJpEnviromentRule[] getEnviromentRules() {
    return enviromentRules;
  }

  public void setEnviromentRules(XmlJpEnviromentRule[] enviromentRules) {
    this.enviromentRules = enviromentRules;
  }

  @Override
  public String toString() {
    return "XmlJpEnviromentRules{" +
        "enviromentRules=" + Arrays.toString(enviromentRules) +
        '}';
  }
}
