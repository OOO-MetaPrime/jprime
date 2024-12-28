package mp.jprime.security.abac.xmlloader.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class XmlJpCollectionCond {
  @JacksonXmlProperty(localName = "in")
  private XmlJpValues in;
  @JacksonXmlProperty(localName = "notIn")
  private XmlJpValues notIn;

  @JacksonXmlProperty(localName = "isNull")
  private XmlJpValues isNull;

  @JacksonXmlProperty(localName = "isNotNull")
  private XmlJpValues isNotNull;

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

  public XmlJpValues getIsNull() {
    return isNull;
  }

  public void setIsNull(XmlJpValues isNull) {
    this.isNull = isNull;
  }

  public XmlJpValues getIsNotNull() {
    return isNotNull;
  }

  public void setIsNotNull(XmlJpValues isNotNull) {
    this.isNotNull = isNotNull;
  }

  @Override
  public String toString() {
    return "XmlJpCollectionCond{" +
        "in='" + in + '\'' +
        ", notIn='" + notIn + '\'' +
        ", isNull='" + isNull + '\'' +
        ", isNotNull='" + isNotNull + '\'' +
        '}';
  }
}
