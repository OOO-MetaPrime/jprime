package mp.jprime.utils.loaders.xml.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "jpResult")
public class XmlJpModeResult {
  private String type;
  private String description;
  private boolean changeData;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isChangeData() {
    return changeData;
  }

  public void setChangeData(boolean changeData) {
    this.changeData = changeData;
  }

  @Override
  public String toString() {
    return "XmlJpModeResult{" +
        "type=" + type +
        ",description=" + description +
        ",changeData=" + changeData +
        '}';
  }
}
