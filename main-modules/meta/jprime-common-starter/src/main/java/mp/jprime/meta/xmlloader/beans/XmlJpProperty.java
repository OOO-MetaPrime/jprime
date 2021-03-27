package mp.jprime.meta.xmlloader.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class XmlJpProperty {
  private String code;
  private String qName;
  private String name;
  private String shortName;
  private String description;
  private boolean mandatory;
  private boolean multiple;
  private String type;
  private Integer length;
  private String refJpClassCode;
  private String refJpAttrCode;
  @JacksonXmlProperty(localName = "jpProps")
  private XmlJpProps schemaProps;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Integer getLength() {
    return length;
  }

  public void setLength(Integer length) {
    this.length = length;
  }

  public boolean isMultiple() {
    return multiple;
  }

  public void setMultiple(boolean multiple) {
    this.multiple = multiple;
  }

  public boolean isMandatory() {
    return mandatory;
  }

  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
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

  public String getRefJpClassCode() {
    return refJpClassCode;
  }

  public void setRefJpClassCode(String refJpClassCode) {
    this.refJpClassCode = refJpClassCode;
  }

  public String getRefJpAttrCode() {
    return refJpAttrCode;
  }

  public void setRefJpAttrCode(String refJpAttrCode) {
    this.refJpAttrCode = refJpAttrCode;
  }

  public XmlJpProps getSchemaProps() {
    return schemaProps;
  }

  public void setSchemaProps(XmlJpProps schemaProps) {
    this.schemaProps = schemaProps;
  }

  @Override
  public String toString() {
    return "XmlJpProperty{" +
        "code='" + code + '\'' +
        ", type=" + type +
        (length != null ? ", length='" + length + '\'' : "") +
        ", multiple=" + multiple +
        ", mandatory=" + mandatory +
        ", name='" + name + '\'' +
        ", shortName='" + shortName + '\'' +
        ", description='" + description + '\'' +
        ", qName='" + qName + '\'' +
        ", refJpClassCode='" + refJpClassCode + '\'' +
        ", refJpAttrCode='" + refJpAttrCode + '\'' +
        ", schemaProps=" + schemaProps +
        '}';
  }
}
