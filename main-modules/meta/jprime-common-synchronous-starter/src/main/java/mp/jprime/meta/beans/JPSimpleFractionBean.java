package mp.jprime.meta.beans;

import mp.jprime.meta.JPSimpleFraction;

/**
 * Бин JPSimpleFraction
 */
public final class JPSimpleFractionBean implements JPSimpleFraction {
  /**
   * Атрибут для хранения - Целая часть дроби
   */
  private final String integerAttrCode;
  /**
   * Атрибут для хранения - Числитель дроби
   */
  private final String numeratorAttrCode;
  /**
   * Атрибут для хранения - Знаменатель дроби
   */
  private final String denominatorAttrCode;

  private JPSimpleFractionBean(String integerAttrCode, String numeratorAttrCode, String denominatorAttrCode) {
    this.integerAttrCode = integerAttrCode != null && !integerAttrCode.isEmpty() ? integerAttrCode : null;
    this.numeratorAttrCode = numeratorAttrCode != null && !numeratorAttrCode.isEmpty() ? numeratorAttrCode : null;
    this.denominatorAttrCode = denominatorAttrCode != null && !denominatorAttrCode.isEmpty() ? denominatorAttrCode : null;
  }

  @Override
  public String getIntegerAttrCode() {
    return integerAttrCode;
  }

  @Override
  public String getNumeratorAttrCode() {
    return numeratorAttrCode;
  }

  @Override
  public String getDenominatorAttrCode() {
    return denominatorAttrCode;
  }

  /**
   * Построитель JPSimpleFractionBean
   *
   * @param numeratorAttrCode Числитель дроби
   * @return Builder
   */
  public static Builder newBuilder(String numeratorAttrCode) {
    return new Builder(numeratorAttrCode);
  }

  /**
   * Построитель JPSimpleFractionBean
   */
  public static final class Builder {
    /**
     * Атрибут для хранения - Целая часть дроби
     */
    private String integerAttrCode;
    /**
     * Атрибут для хранения - Числитель дроби
     */
    private final String numeratorAttrCode;
    /**
     * Атрибут для хранения - Знаменатель дроби
     */
    private String denominatorAttrCode;

    private Builder(String numeratorAttrCode) {
      this.numeratorAttrCode = numeratorAttrCode;
    }

    /**
     * Создаем JPSimpleFractionBean
     *
     * @return JPSimpleFractionBean
     */
    public JPSimpleFractionBean build() {
      return new JPSimpleFractionBean(integerAttrCode, numeratorAttrCode, denominatorAttrCode);
    }

    /**
     * Атрибут для хранения - Целая часть дроби
     *
     * @param integerAttrCode Кодовое имя атрибута
     * @return Builder
     */
    public Builder integerAttrCode(String integerAttrCode) {
      this.integerAttrCode = integerAttrCode;
      return this;
    }

    /**
     * Атрибут для хранения - Знаменатель дроби
     *
     * @param denominatorAttrCode Кодовое имя атрибутае
     * @return Builder
     */
    public Builder denominatorAttrCode(String denominatorAttrCode) {
      this.denominatorAttrCode = denominatorAttrCode;
      return this;
    }
  }
}
