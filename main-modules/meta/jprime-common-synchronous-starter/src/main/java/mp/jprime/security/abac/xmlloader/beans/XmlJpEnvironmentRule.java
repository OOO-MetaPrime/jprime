package mp.jprime.security.abac.xmlloader.beans;

public class XmlJpEnvironmentRule {
  private String name;
  private String qName;
  private String effect;
  private XmlJpTime time;
  private XmlJpCollectionCond ip;

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

  public XmlJpTime getTime() {
    return time;
  }

  public void setTime(XmlJpTime time) {
    this.time = time;
  }

  public XmlJpCollectionCond getIp() {
    return ip;
  }

  public void setIp(XmlJpCollectionCond ip) {
    this.ip = ip;
  }

  @Override
  public String toString() {
    return "XmlJpEnvironmentRule{" +
        "name='" + name + '\'' +
        ", qName='" + qName + '\'' +
        ", effect='" + effect + '\'' +
        ", time='" + time + '\'' +
        ", ip='" + ip + '\'' +
        '}';
  }
}
