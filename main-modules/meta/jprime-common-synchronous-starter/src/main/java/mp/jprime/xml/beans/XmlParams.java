package mp.jprime.xml.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Arrays;

@JacksonXmlRootElement(localName = "params")
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
