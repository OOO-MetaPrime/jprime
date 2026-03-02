package mp.jprime.xml.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Arrays;

@JacksonXmlRootElement(localName = "paramEnums")
@JsonIgnoreProperties(ignoreUnknown = true)
public class XmlParamEnums {
  @JacksonXmlProperty(localName = "paramEnum")
  @JacksonXmlElementWrapper(useWrapping = false)
  private XmlParamEnum[] xmlParamEnums;

  public XmlParamEnum[] getXmlParamEnums() {
    return xmlParamEnums;
  }

  public void setXmlParamEnums(XmlParamEnum[] xmlParamEnums) {
    this.xmlParamEnums = xmlParamEnums;
  }

  @Override
  public String toString() {
    return "XmlParamEnums{" +
        "xmlParamEnums=" + Arrays.toString(xmlParamEnums) +
        '}';
  }
}