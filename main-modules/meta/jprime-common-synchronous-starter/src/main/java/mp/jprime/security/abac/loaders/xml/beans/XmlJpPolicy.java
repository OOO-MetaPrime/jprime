package mp.jprime.security.abac.loaders.xml.beans;

public class XmlJpPolicy {
  private String name;

  private String qName;

  private XmlJpActions actions;
  private XmlJpEnvironmentRules environmentRules;
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

  public XmlJpEnvironmentRules getEnvironmentRules() {
    return environmentRules;
  }

  public void setEnvironmentRules(XmlJpEnvironmentRules environmentRules) {
    this.environmentRules = environmentRules;
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
        ", environmentRules='" + environmentRules + '\'' +
        ", resourceRules='" + resourceRules + '\'' +
        ", subjectRules='" + subjectRules + '\'' +
        '}';
  }
}
