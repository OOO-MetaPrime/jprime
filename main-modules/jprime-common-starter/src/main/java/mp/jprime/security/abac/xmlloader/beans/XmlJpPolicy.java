package mp.jprime.security.abac.xmlloader.beans;

public class XmlJpPolicy {
  private String name;

  private String qName;

  private XmlJpActions actions;
  private XmlJpEnviromentRules enviromentRules;
  private XmlJpResourceRules resourceRules;
  private XmlJpSubjectRules subjectRules;

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

  public XmlJpActions getActions() {
    return actions;
  }

  public void setActions(XmlJpActions actions) {
    this.actions = actions;
  }

  public XmlJpEnviromentRules getEnviromentRules() {
    return enviromentRules;
  }

  public void setEnviromentRules(XmlJpEnviromentRules enviromentRules) {
    this.enviromentRules = enviromentRules;
  }

  public XmlJpResourceRules getResourceRules() {
    return resourceRules;
  }

  public void setResourceRules(XmlJpResourceRules resourceRules) {
    this.resourceRules = resourceRules;
  }

  public XmlJpSubjectRules getSubjectRules() {
    return subjectRules;
  }

  public void setSubjectRules(XmlJpSubjectRules subjectRules) {
    this.subjectRules = subjectRules;
  }

  @Override
  public String toString() {
    return "XmlJpPolicy{" +
        "name='" + name + '\'' +
        ", qName='" + qName + '\'' +
        ", actions='" + actions + '\'' +
        ", enviromentRules='" + enviromentRules + '\'' +
        ", resourceRules='" + resourceRules + '\'' +
        ", subjectRules='" + subjectRules + '\'' +
        '}';
  }
}
