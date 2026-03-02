package mp.jprime.utils.loaders.xml.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Arrays;

@JacksonXmlRootElement(localName = "jpUtil")
@JsonIgnoreProperties(ignoreUnknown = true)
public class XmlJpUtils {
  @JacksonXmlElementWrapper(useWrapping = false)
  private XmlJpUtil[] jpUtil;

  public XmlJpUtil[] getJpUtil() {
    return jpUtil;
  }

  public void setJpUtil(XmlJpUtil[] jpUtil) {
    this.jpUtil = jpUtil;
  }

  @Override
  public String toString() {
    return "XmlJpUtils{" +
        "jpUtil=" + Arrays.toString(jpUtil) +
        '}';
  }
}
