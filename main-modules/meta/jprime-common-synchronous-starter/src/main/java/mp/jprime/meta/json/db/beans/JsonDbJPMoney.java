package mp.jprime.meta.json.db.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class JsonDbJPMoney {
  /**
   * Код валюты
   */
  private String currencyCode;

  public JsonDbJPMoney() {

  }

  private JsonDbJPMoney(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  public static JsonDbJPMoney of(String currencyCode) {
    return new JsonDbJPMoney(currencyCode);
  }

  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }
}
