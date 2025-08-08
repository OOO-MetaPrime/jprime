package mp.jprime.meta.loaders.xml.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class XmlJpSimpleFraction {
  private String integerAttrCode;
  private String denominatorAttrCode;

  public String getIntegerAttrCode() {
    return integerAttrCode;
  }

  public void setIntegerAttrCode(String integerAttrCode) {
    this.integerAttrCode = integerAttrCode;
  }

  public String getDenominatorAttrCode() {
    return denominatorAttrCode;
  }

  public void setDenominatorAttrCode(String denominatorAttrCode) {
    this.denominatorAttrCode = denominatorAttrCode;
  }

  @Override
  public String toString() {
    return "XmlJpSimpleFraction{" +
        "integerAttrCode='" + integerAttrCode + '\'' +
        ", denominatorAttrCode='" + denominatorAttrCode + '\'' +
        '}';
  }
}
