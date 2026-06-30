package mp.jprime.xml.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import tools.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Arrays;

@JsonRootName(value = "params")
@JsonIgnoreProperties(ignoreUnknown = true)
public class XmlParams {
  @JacksonXmlProperty(localName = "param")
  @JacksonXmlElementWrapper(useWrapping = false)
  private XmlParam[] xmlParams;

  public XmlParam[] getXmlParams() {
    return xmlParams;
  }

  public void setXmlParams(XmlParam[] xmlParams) {
    this.xmlParams = xmlParams;
  }

  @Override
  public String toString() {
    return "XmlParams{" +
        "xmlParams=" + Arrays.toString(xmlParams) +
        '}';
  }
}
