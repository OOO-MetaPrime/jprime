package mp.jprime.utils.loaders.xml.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "jpUtilSettings")
public class XmlJpUtilSettings {
  private XmlJpUtils jpUtils;

  public XmlJpUtils getJpUtils() {
    return jpUtils;
  }

  public void setJpUtils(XmlJpUtils jpUtils) {
    this.jpUtils = jpUtils;
  }

  @Override
  public String toString() {
    return "XmlJpUtilSettings{" +
        "jpUtils=" + jpUtils +
        '}';
  }
}
