package mp.jprime.meta.beans;

import mp.jprime.meta.JPMoney;

/**
 * Бин JPMoney
 */
public final class JPMoneyBean implements JPMoney {
  /**
   * Атрибут для хранения
   */
  private final String attrCode;
  /**
   * Код валюты
   */
  private final String currencyCode;

  private JPMoneyBean(String attrCode, String currencyCode) {
    this.attrCode = attrCode;
    this.currencyCode = currencyCode;
  }

  /**
   * Атрибут для хранения
   *
   * @return Кодовое имя атрибута
   */
  @Override
  public String getAttrCode() {
    return attrCode;
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

  /**
   * Построитель JPMoneyBean
   *
   * @param numeratorAttrCode Числитель дроби
   * @return Builder
   */
  public static Builder newBuilder(String numeratorAttrCode) {
    return new Builder(numeratorAttrCode);
  }

  /**
   * Построитель JPMoneyBean
   */
  public static final class Builder {
    /**
     * Атрибут для хранения
     */
    private String attrCode;
    /**
     * Код валюты
     */
    private String currencyCode;

    private Builder(String attrCode) {
      this.attrCode = attrCode;
    }

    /**
     * Создаем JPMoneyBean
     *
     * @return JPMoneyBean
     */
    public JPMoneyBean build() {
      return new JPMoneyBean(attrCode, currencyCode);
    }

    /**
     * Код валюты
     *
     * @param currencyCode Код валюты
     * @return Builder
     */
    public Builder currencyCode(String currencyCode) {
      this.currencyCode = currencyCode;
      return this;
    }
  }
}
