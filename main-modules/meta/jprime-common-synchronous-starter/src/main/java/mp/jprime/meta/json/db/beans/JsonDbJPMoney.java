package mp.jprime.meta.json.db.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
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
