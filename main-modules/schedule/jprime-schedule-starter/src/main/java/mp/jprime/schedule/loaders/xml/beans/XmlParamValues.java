package mp.jprime.schedule.loaders.xml.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tools.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.Arrays;

@JsonRootName(value = "paramValues")
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
