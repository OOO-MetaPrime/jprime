package mp.jprime.meta.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class JsonJPMoney {
  /**
   * Код валюты
   */
  private String currencyCode;

  public String getCurrencyCode() {
    return currencyCode;
  }

  public JsonJPMoney() {

  }

  private JsonJPMoney(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  /**
   * Построитель JsonJPMoney
   *
   * @return Builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Построитель JsonJPMoney
   */
  public static final class Builder {
    private String currencyCode;

    private Builder() {

    }

    public Builder currencyCode(String currencyCode) {
      this.currencyCode = currencyCode;
      return this;
    }

    public JsonJPMoney build() {
      return new JsonJPMoney(currencyCode);
    }
  }
}
