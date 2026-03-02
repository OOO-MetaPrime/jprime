package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Простая дробь
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonSimpleFraction {
  /**
   * Признак положительной дроби
   */
  private boolean positive = true;
  /**
   * Целая часть дроби
   */
  private Integer integer;
  /**
   * Числитель дроби
   */
  private Integer numerator;
  /**
   * Знаменатель дроби
   */
  private Integer denominator;

  public boolean isPositive() {
    return positive;
  }

  public void setPositive(boolean positive) {
    this.positive = positive;
  }

  public Integer getInteger() {
    return integer;
  }

  public void setInteger(Integer integer) {
    this.integer = integer;
  }

  public Integer getNumerator() {
    return numerator;
  }

  public void setNumerator(Integer numerator) {
    this.numerator = numerator;
  }

  public Integer getDenominator() {
    return denominator;
  }

  public void setDenominator(Integer denominator) {
    this.denominator = denominator;
  }
}