package mp.jprime.schedule.loaders.xml.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Arrays;

@JacksonXmlRootElement(localName = "paramValues")
@JsonIgnoreProperties(ignoreUnknown = true)
public class XmlParamValues {
  @JacksonXmlProperty(localName = "paramValue")
  @JacksonXmlElementWrapper(useWrapping = false)
  private XmlParamValue[] paramValue;

  public XmlParamValue[] getParamValue() {
    return paramValue;
  }

  public void setParamValue(XmlParamValue[] paramValue) {
    this.paramValue = paramValue;
  }

  @Override
  public String toString() {
    return "XmlParamValues{" +
        "paramValue=" + Arrays.toString(paramValue) +
        '}';
  }
}
