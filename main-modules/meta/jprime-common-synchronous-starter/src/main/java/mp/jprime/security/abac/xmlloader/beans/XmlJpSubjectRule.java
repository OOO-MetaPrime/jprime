package mp.jprime.security.abac.xmlloader.beans;

public class XmlJpSubjectRule {
  private String name;
  private String qName;
  private String effect;
  private XmlJpCollectionCond username;
  private XmlJpCollectionCond role;
  private XmlJpCollectionCond orgId;
  private XmlJpCollectionCond depId;

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

  public String getEffect() {
    return effect;
  }

  public void setEffect(String effect) {
    this.effect = effect;
  }

  public XmlJpCollectionCond getUsername() {
    return username;
  }

  public void setUsername(XmlJpCollectionCond username) {
    this.username = username;
  }

  public XmlJpCollectionCond getRole() {
    return role;
  }

  public void setRole(XmlJpCollectionCond role) {
    this.role = role;
  }

  public XmlJpCollectionCond getOrgId() {
    return orgId;
  }

  public void setOrgId(XmlJpCollectionCond orgId) {
    this.orgId = orgId;
  }

  public XmlJpCollectionCond getDepId() {
    return depId;
  }

  public void setDepId(XmlJpCollectionCond depId) {
    this.depId = depId;
  }

  @Override
  public String toString() {
    return "XmlJpSubjectRule{" +
        "name='" + name + '\'' +
        ", qName='" + qName + '\'' +
        ", effect='" + effect + '\'' +
        ", username='" + username + '\'' +
        ", role='" + role + '\'' +
        ", orgId='" + orgId + '\'' +
        ", depId='" + depId + '\'' +
        '}';
  }
}
