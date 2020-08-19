package mp.jprime.meta.xmlloader.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class XmlJpAttr {
  private String guid;
  private String name;
  private String shortName;
  private String description;
  private String qName;
  private String code;
  private String jpPackage;
  private boolean identifier;
  private boolean mandatory;
  private String type;
  private Integer length;
  private String refJpClass;
  private String refJpAttr;
  private String virtualReference;
  private String virtualType;
  private XmlJpFile refJpFile;

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

  public boolean isIdentifier() {
    return identifier;
  }

  public void setIdentifier(boolean identifier) {
    this.identifier = identifier;
  }

  public boolean isMandatory() {
    return mandatory;
  }

  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getRefJpClass() {
    return refJpClass;
  }

  public void setRefJpClass(String refJpClass) {
    this.refJpClass = refJpClass;
  }

  public String getRefJpAttr() {
    return refJpAttr;
  }

  public void setRefJpAttr(String refJpAttr) {
    this.refJpAttr = refJpAttr;
  }

  public Integer getLength() {
    return length;
  }

  public void setLength(Integer length) {
    this.length = length;
  }

  public String getVirtualReference() {
    return virtualReference;
  }

  public void setVirtualReference(String virtualReference) {
    this.virtualReference = virtualReference;
  }

  public String getVirtualType() {
    return virtualType;
  }

  public void setVirtualType(String virtualType) {
    this.virtualType = virtualType;
  }

  public XmlJpFile getRefJpFile() {
    return refJpFile;
  }

  public void setRefJpFile(XmlJpFile refJpFile) {
    this.refJpFile = refJpFile;
  }

  @Override
  public String toString() {
    return "XmlJpAttr{" +
        "guid='" + guid + '\'' +
        ", name='" + name + '\'' +
        ", shortName='" + shortName + '\'' +
        ", name='" + description + '\'' +
        ", qName='" + qName + '\'' +
        ", code='" + code + '\'' +
        ", jpPackage='" + jpPackage + '\'' +
        ", identifier='" + identifier + '\'' +
        ", mandatory='" + mandatory + '\'' +
        ", type='" + type + '\'' +
        ", refJpClass='" + refJpClass + '\'' +
        ", refJpAttr='" + refJpAttr + '\'' +
        ", refJpFile='" + refJpFile + '\'' +
        ", virtualReference='" + virtualReference + '\'' +
        ", virtualType='" + virtualType + '\'' +
        (length != null ? ", length='" + length + '\'' : "") +
        '}';
  }
}
