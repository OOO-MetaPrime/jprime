package mp.jprime.utils.loaders.xml.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Arrays;

@JacksonXmlRootElement(localName = "jpDefValues")
@JsonIgnoreProperties(ignoreUnknown = true)
public class XmlJpDefValues {
  @JacksonXmlProperty(localName = "param")
  @JacksonXmlElementWrapper(useWrapping = false)
  private XmlJpDefValuesParam[] param;

  public XmlJpDefValuesParam[] getParam() {
    return param;
  }

  public void setParam(XmlJpDefValuesParam[] param) {
    this.param = param;
  }

  @Override
  public String toString() {
    return "XmlJpDefValues{" +
        "param=" + Arrays.toString(param) +
        '}';
  }
}
