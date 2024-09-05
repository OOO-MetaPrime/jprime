package mp.jprime.meta.json.db.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class JsonDbJPMoney {
  /**
   * Код валюты
   */
  private String currencyCode;

  public String getCurrencyCode() {
    return currencyCode;
  }

  public JsonDbJPMoney() {

  }

  private JsonDbJPMoney(String currencyCode) {
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

    public JsonDbJPMoney build() {
      return new JsonDbJPMoney(currencyCode);
    }
  }
}
