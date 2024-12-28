package mp.jprime.meta.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class JsonJPMoney {
  /**
   * Код валюты
   */
  private String currencyCode;

  public JsonJPMoney() {

  }

  private JsonJPMoney(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  public static JsonJPMoney of(String currencyCode) {
    return new JsonJPMoney(currencyCode);
  }

  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }
}
