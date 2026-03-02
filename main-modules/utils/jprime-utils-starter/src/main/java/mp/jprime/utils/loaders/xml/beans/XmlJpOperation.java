package mp.jprime.utils.loaders.xml.beans;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "operation")
public class XmlJpOperation {
  @JacksonXmlProperty(isAttribute = true)
  private String type;
  @JacksonXmlProperty(isAttribute = true)
  private String alias;
  @JacksonXmlProperty(isAttribute = true)
  private String forAlias;
  @JsonDeserialize(using = XmlInnerDeserializer.class)
  private String settings;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public String getForAlias() {
    return forAlias;
  }

  public void setForAlias(String forAlias) {
    this.forAlias = forAlias;
  }

  public String getSettings() {
    return settings;
  }

  public void setSettings(String settings) {
    this.settings = settings;
  }

  @Override
  public String toString() {
    return "XmlJpOperation{" +
        "type=" + type +
        ",settings=" + settings +
        ",alias=" + alias +
        ",forAlias=" + forAlias +
        '}';
  }
}
