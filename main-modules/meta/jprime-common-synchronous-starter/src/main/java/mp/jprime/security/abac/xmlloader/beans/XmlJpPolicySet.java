package mp.jprime.security.abac.xmlloader.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class XmlJpPolicySet {
  private String code;
  private String name;
  private String qName;
  @JacksonXmlProperty(localName = "jpClasses")
  private XmlJpClasses jpClasses;
  @JacksonXmlProperty(localName = "jpPolicies")
  private XmlJpPolicies jpPolicies;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getqName() {
    return qName;
  }

  public void setqName(String qName) {
    this.qName = qName;
  }

  public XmlJpClasses getJpClasses() {
    return jpClasses;
  }

  public void setJpClasses(XmlJpClasses jpClasses) {
    this.jpClasses = jpClasses;
  }

  public XmlJpPolicies getJpPolicies() {
    return jpPolicies;
  }

  public void setJpPolicies(XmlJpPolicies jpPolicies) {
    this.jpPolicies = jpPolicies;
  }

  @Override
  public String toString() {
    return "XmlJpPackage{" +
        "name='" + name + '\'' +
        ", qName='" + qName + '\'' +
        ", jpClasses='" + jpClasses + '\'' +
        ", jpPolicies='" + jpPolicies + '\'' +
        '}';
  }
}
