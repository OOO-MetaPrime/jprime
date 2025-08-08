package mp.jprime.metamaps.loaders.xml.beans;

public class XmlJpAttrMap {
  private String code;
  private String map;
  private String fuzzyMap;
  private String cs;
  private Boolean readOnly;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMap() {
    return map;
  }

  public void setMap(String map) {
    this.map = map;
  }

  public String getFuzzyMap() {
    return fuzzyMap;
  }

  public void setFuzzyMap(String fuzzyMap) {
    this.fuzzyMap = fuzzyMap;
  }

  public String getCs() {
    return cs;
  }

  public void setCs(String cs) {
    this.cs = cs;
  }

  public Boolean getReadOnly() {
    return readOnly;
  }

  public void setReadOnly(Boolean readOnly) {
    this.readOnly = readOnly;
  }

  @Override
  public String toString() {
    return "XmlJpAttrMap{" +
        "code='" + code + '\'' +
        ", map='" + map + '\'' +
        ", cs='" + cs + '\'' +
        ", readOnly='" + readOnly + '\'' +
        '}';
  }
}