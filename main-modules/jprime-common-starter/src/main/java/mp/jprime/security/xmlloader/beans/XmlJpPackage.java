package mp.jprime.security.xmlloader.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class XmlJpPackage {
  private String code;
  private String description;
  private String qName;
  @JacksonXmlProperty(localName = "jpPermitAccess")
  private XmlJpPermitAccess jpPermitAccess;
  @JacksonXmlProperty(localName = "jpProhibitionAccess")
  private XmlJpProhibitionAccess jpProhibitionAccess;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getqName() {
    return qName;
  }

  public void setqName(String qName) {
    this.qName = qName;
  }

  public XmlJpPermitAccess getJpPermitAccess() {
    return jpPermitAccess;
  }

  public void setJpPermitAccess(XmlJpPermitAccess jpPermitAccess) {
    this.jpPermitAccess = jpPermitAccess;
  }

  public XmlJpProhibitionAccess getJpProhibitionAccess() {
    return jpProhibitionAccess;
  }

  public void setJpProhibitionAccess(XmlJpProhibitionAccess jpProhibitionAccess) {
    this.jpProhibitionAccess = jpProhibitionAccess;
  }

  @Override
  public String toString() {
    return "XmlJpPackage{" +
        "code='" + code + '\'' +
        ", name='" + description + '\'' +
        ", qName='" + qName + '\'' +
        ", jpPermitAccess='" + jpPermitAccess +
        ", jpProhibitionAccess='" + jpProhibitionAccess +
        '}';
  }
}
