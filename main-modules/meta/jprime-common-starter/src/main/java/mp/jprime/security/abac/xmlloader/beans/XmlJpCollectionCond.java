package mp.jprime.security.abac.xmlloader.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class XmlJpCollectionCond {
  @JacksonXmlProperty(localName = "in")
  private XmlJpValues in;
  @JacksonXmlProperty(localName = "notIn")
  private XmlJpValues notIn;

  public XmlJpValues getIn() {
    return in;
  }

  public void setIn(XmlJpValues in) {
    this.in = in;
  }

  public XmlJpValues getNotIn() {
    return notIn;
  }

  public void setNotIn(XmlJpValues notIn) {
    this.notIn = notIn;
  }

  @Override
  public String toString() {
    return "XmlJpCollectionCond{" +
        "in='" + in + '\'' +
        ", notIn='" + notIn + '\'' +
        '}';
  }
}
