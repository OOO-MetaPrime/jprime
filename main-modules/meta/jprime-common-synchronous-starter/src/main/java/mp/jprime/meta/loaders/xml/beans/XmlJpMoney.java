package mp.jprime.meta.loaders.xml.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class XmlJpMoney {
  private String currencyCode;

  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  @Override
  public String toString() {
    return "XmlJpMoney{" +
        "currencyCode='" + currencyCode + '\'' +
        '}';
  }
}
