package mp.jprime.metamaps.loaders.xml.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class XmlJpClassMap {
  private String code;
  private String storage;
  private String map;
  private String schema;
  @JacksonXmlProperty(localName = "jpAttrMaps")
  private XmlJpAttrMaps jpAttrMaps;

  public XmlJpAttrMaps getJpAttrMaps() {
    return jpAttrMaps;
  }

  public void setJpAttrMaps(XmlJpAttrMaps jpAttrMaps) {
    this.jpAttrMaps = jpAttrMaps;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getStorage() {
    return storage;
  }

  public void setStorage(String storage) {
    this.storage = storage;
  }

  public String getMap() {
    return map;
  }

  public void setMap(String map) {
    this.map = map;
  }

  public String getSchema() {
    return schema;
  }

  public void setSchema(String schema) {
    this.schema = schema;
  }

  @Override
  public String toString() {
    return "XmlJpClassMap{" +
        "code='" + code + '\'' +
        ", storage='" + storage + '\'' +
        ", map='" + map + '\'' +
        ", schema='" + schema + '\'' +
        ", jpAttrMaps=" + jpAttrMaps +
        '}';
  }
}
