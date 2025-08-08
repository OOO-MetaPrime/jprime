package mp.jprime.meta.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import mp.jprime.meta.JPMoney;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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

  public static JsonJPMoney toJson(JPMoney money) {
    return money == null ? null : JsonJPMoney.of(money.getCurrencyCode());
  }
}
