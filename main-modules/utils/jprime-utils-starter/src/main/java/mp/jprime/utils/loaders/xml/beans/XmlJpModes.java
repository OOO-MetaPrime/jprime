package mp.jprime.utils.loaders.xml.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Arrays;

@JacksonXmlRootElement(localName = "jpMode")
@JsonIgnoreProperties(ignoreUnknown = true)
public class XmlJpModes {
  @JacksonXmlElementWrapper(useWrapping = false)
  private XmlJpMode[] jpMode;

  public XmlJpMode[] getJpMode() {
    return jpMode;
  }

  public void setJpMode(XmlJpMode[] jpMode) {
    this.jpMode = jpMode;
  }

  @Override
  public String toString() {
    return "XmlJpModes{" +
        "jpMode=" + Arrays.toString(jpMode) +
        '}';
  }
}
