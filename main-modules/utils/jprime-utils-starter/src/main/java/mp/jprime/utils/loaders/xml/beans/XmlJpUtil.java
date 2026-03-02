package mp.jprime.utils.loaders.xml.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "jpUtil")
public class XmlJpUtil {
  private boolean disable;
  private String code;
  private String description;
  private String title;
  private String qName;
  private String jpPackage;
  private XmlJpViewRoles roles;
  private XmlJpViewClasses jpClasses;
  private XmlJpClassTags jpClassTags;
  private XmlJpUtilTags jpUtilTags;
  private XmlJpDefValues jpDefValues;
  private XmlJpCheck jpCheck;
  private XmlJpModes jpModes;

  public boolean isDisable() {
    return disable;
  }

  public void setDisable(boolean disable) {
    this.disable = disable;
  }

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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getqName() {
    return qName;
  }

  public void setqName(String qName) {
    this.qName = qName;
  }

  public String getJpPackage() {
    return jpPackage;
  }

  public void setJpPackage(String jpPackage) {
    this.jpPackage = jpPackage;
  }

  public XmlJpViewRoles getRoles() {
    return roles;
  }

  public void setRoles(XmlJpViewRoles roles) {
    this.roles = roles;
  }

  public XmlJpViewClasses getJpClasses() {
    return jpClasses;
  }

  public void setJpClasses(XmlJpViewClasses jpClasses) {
    this.jpClasses = jpClasses;
  }

  public XmlJpClassTags getJpClassTags() {
    return jpClassTags;
  }

  public void setJpClassTags(XmlJpClassTags jpClassTags) {
    this.jpClassTags = jpClassTags;
  }

  public XmlJpUtilTags getJpUtilTags() {
    return jpUtilTags;
  }

  public void setJpUtilTags(XmlJpUtilTags jpUtilTags) {
    this.jpUtilTags = jpUtilTags;
  }

  public XmlJpDefValues getJpDefValues() {
    return jpDefValues;
  }

  public void setJpDefValues(XmlJpDefValues jpDefValues) {
    this.jpDefValues = jpDefValues;
  }

  public XmlJpCheck getJpCheck() {
    return jpCheck;
  }

  public void setJpCheck(XmlJpCheck jpCheck) {
    this.jpCheck = jpCheck;
  }

  public XmlJpModes getJpModes() {
    return jpModes;
  }

  public void setJpModes(XmlJpModes jpModes) {
    this.jpModes = jpModes;
  }

  @Override
  public String toString() {
    return "XmlJpUtil{" +
        "code=" + code +
        ",description=" + description +
        ",title=" + title +
        ",qName=" + qName +
        ",jpPackage=" + jpPackage +
        ",roles=" + roles +
        ",jpClasses=" + jpClasses +
        ",jpClassTags=" + jpClassTags +
        ",jpUtilTags=" + jpUtilTags +
        ",jpDefValues=" + jpDefValues +
        ",jpCheck=" + jpCheck +
        ",jpModes=" + jpModes +
        '}';
  }
}
