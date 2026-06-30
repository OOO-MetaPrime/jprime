package mp.jprime.utils.loaders.xml.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tools.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.Arrays;

@JsonRootName(value = "jpDefValues")
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
