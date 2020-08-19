package mp.jprime.security.abac.xmlloader.beans;

public class XmlJpResourceRule {
  private String name;
  private String qName;
  private String effect;
  private String attr;
  private XmlJpCollectionCond cond;

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

  public String getAttr() {
    return attr;
  }

  public void setAttr(String attr) {
    this.attr = attr;
  }

  public XmlJpCollectionCond getCond() {
    return cond;
  }

  public void setCond(XmlJpCollectionCond cond) {
    this.cond = cond;
  }

  @Override
  public String toString() {
    return "XmlJpResourceRule{" +
        "name='" + name + '\'' +
        ", qName='" + qName + '\'' +
        ", effect='" + effect + '\'' +
        ", attr='" + attr + '\'' +
        ", cond='" + cond + '\'' +
        '}';
  }
}
