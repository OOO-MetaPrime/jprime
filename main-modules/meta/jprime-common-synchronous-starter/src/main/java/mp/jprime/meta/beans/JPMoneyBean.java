package mp.jprime.meta.beans;

import mp.jprime.meta.JPMoney;

/**
 * Бин JPMoney
 */
public final class JPMoneyBean implements JPMoney {
  /**
   * Код валюты
   */
  private final String currencyCode;

  private JPMoneyBean(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  /**
   * Код валюты
   *
   * @return Код валюты
   */
  @Override
  public String getCurrencyCode() {
    return currencyCode;
  }

  public static JPMoney of(String currencyCode) {
    return new JPMoneyBean(currencyCode);
  }
}
