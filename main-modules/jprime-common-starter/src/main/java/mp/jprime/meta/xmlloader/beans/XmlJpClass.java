package mp.jprime.meta.xmlloader.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class XmlJpClass {
  private String guid;
  private String name;
  private String shortName;
  private String description;
  private String qName;
  private String code;
  private String jpPackage;
  @JacksonXmlProperty(localName = "jpAttrs")
  private XmlJpAttrs jpAttrs;
  private String pluralCode;

  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getShortName() {
    return shortName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
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

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getJpPackage() {
    return jpPackage;
  }

  public void setJpPackage(String jpPackage) {
    this.jpPackage = jpPackage;
  }

  public XmlJpAttrs getJpAttrs() {
    return jpAttrs;
  }

  public void setJpAttrs(XmlJpAttrs jpAttrs) {
    this.jpAttrs = jpAttrs;
  }

  public String getPluralCode() {
    return pluralCode;
  }

  public void setPluralCode(String pluralCode) {
    this.pluralCode = pluralCode;
  }

  @Override
  public String toString() {
    return "XmlJpClass{" +
        "guid='" + guid + '\'' +
        ", name='" + name + '\'' +
        ", shortName='" + shortName + '\'' +
        ", name='" + description + '\'' +
        ", qName='" + qName + '\'' +
        ", code='" + code + '\'' +
        ", jpPackage='" + jpPackage + '\'' +
        ", jpAttrs=" + jpAttrs +
        ", pluralCode='" + pluralCode + '\'' +
        '}';
  }
}
