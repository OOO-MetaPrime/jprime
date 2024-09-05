package mp.jprime.lang;

import mp.jprime.utils.CurrencyCode;
import org.javamoney.moneta.CurrencyUnitBuilder;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Objects;

public final class JPMoney implements Comparable<JPMoney>, Serializable {
  private static final long serialVersionUID = -2489049794554399911L;

  private static final Map<String, CurrencyUnit> CURRENCY_LIST = Map.of(
      CurrencyCode.RUB, CurrencyUnitBuilder.of(CurrencyCode.RUB, "jpCurrencyBuilder")
          .setNumericCode(643)
          .build()
  );

  private final Money money;

  private JPMoney(Money money) {
    this.money = money;
  }

  private JPMoney(Number number, String currencyCode) {
    CurrencyUnit unit = CURRENCY_LIST.get(currencyCode);
    if (unit != null) {
      this.money = Money.of(number, unit);
    } else {
      this.money = Money.of(number, currencyCode);
    }
  }

  private JPMoney(BigDecimal number, String currencyCode) {
    CurrencyUnit unit = CURRENCY_LIST.get(currencyCode);
    if (unit != null) {
      this.money = Money.of(number, unit);
    } else {
      this.money = Money.of(number, currencyCode);
    }
  }

  /**
   * Возвращает код валюты
   *
   * @return Код вылюты
   */
  public String getCurrencyCode() {
    return money.getCurrency().getCurrencyCode();
  }

  /**
   * stripped number value.
   *
   * @return the stripped number value.
   */
  public BigDecimal getNumberStripped() {
    return money.getNumberStripped();
  }

  /*
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(JPMoney o) {
    Objects.requireNonNull(o);
    return this.money.compareTo(o.money);
  }

  // Arithmetic Operations

  public JPMoney abs() {
    return new JPMoney(money.abs());
  }

  public JPMoney divide(long divisor) {
    return new JPMoney(money.divide(divisor));
  }

  public JPMoney divide(double divisor) {
    return new JPMoney(money.divide(divisor));
  }

  public JPMoney multiply(long multiplicand) {
    return new JPMoney(money.multiply(multiplicand));
  }

  public JPMoney multiply(double multiplicand) {
    return new JPMoney(money.multiply(multiplicand));
  }

  public JPMoney remainder(long divisor) {
    return remainder(BigDecimal.valueOf(divisor));
  }

  public JPMoney remainder(double divisor) {
    return new JPMoney(money.remainder(divisor));
  }

  public boolean isZero() {
    return signum() == 0;
  }

  public boolean isPositive() {
    return signum() == 1;
  }

  public boolean isPositiveOrZero() {
    return signum() >= 0;
  }

  public boolean isNegative() {
    return signum() == -1;
  }

  public boolean isNegativeOrZero() {
    return signum() <= 0;
  }

  public JPMoney add(JPMoney amount) {
    return new JPMoney(this.money.add(amount.money));
  }

  public JPMoney divide(Number divisor) {
    return new JPMoney(this.money.divide(divisor));
  }


  public JPMoney divideToIntegralValue(long divisor) {
    return new JPMoney(this.money.divideToIntegralValue(divisor));
  }

  public JPMoney divideToIntegralValue(double divisor) {
    return new JPMoney(this.money.divideToIntegralValue(divisor));
  }

  public JPMoney divideToIntegralValue(Number divisor) {
    return new JPMoney(this.money.divideToIntegralValue(divisor));
  }

  public JPMoney multiply(Number multiplicand) {
    return new JPMoney(this.money.multiply(multiplicand));
  }

  public JPMoney negate() {
    return new JPMoney(this.money.negate());
  }


  public JPMoney plus() {
    return this;
  }

  public JPMoney subtract(JPMoney subtrahend) {
    return new JPMoney(this.money.subtract(subtrahend.money));
  }

  public JPMoney stripTrailingZeros() {
    return new JPMoney(this.money.stripTrailingZeros());
  }

  public JPMoney remainder(Number divisor) {
    return new JPMoney(this.money.remainder(divisor));
  }

  public JPMoney scaleByPowerOfTen(int power) {
    return new JPMoney(this.money.scaleByPowerOfTen(power));
  }

  public JPMoney round(int value) {
    return new JPMoney(
        this.money.getNumberStripped().setScale(value, RoundingMode.HALF_EVEN),
        this.getCurrencyCode()
    );
  }

  public int signum() {
    return money.signum();
  }

  public boolean isLessThan(JPMoney amount) {
    return this.money.isLessThan(amount.money);
  }

  public boolean isLessThanOrEqualTo(JPMoney amount) {
    return this.money.isLessThanOrEqualTo(amount.money);
  }

  public boolean isGreaterThan(JPMoney amount) {
    return this.money.isGreaterThan(amount.money);
  }

  public boolean isGreaterThanOrEqualTo(JPMoney amount) {
    return this.money.isGreaterThanOrEqualTo(amount.money);
  }

  public boolean isEqualTo(JPMoney amount) {
    return this.money.isEqualTo(amount.money);
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof JPMoney) {
      return money.equals(obj);
    }
    return false;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return money.toString();
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return money.hashCode();
  }

  /**
   * Static factory method for creating a new instance of {@link JPMoney}.
   *
   * @param number       The numeric part, not null.
   * @param currencyCode The target currency as ISO currency code.
   * @return A new instance of {@link Money}.
   */
  public static JPMoney of(Number number, String currencyCode) {
    return new JPMoney(number, currencyCode);
  }

  /**
   * Static factory method for creating a new instance of {@link JPMoney}.
   *
   * @param number       The numeric part, not null.
   * @param currencyCode The target currency as ISO currency code.
   * @return A new instance of {@link Money}.
   */
  public static JPMoney of(BigDecimal number, String currencyCode) {
    return new JPMoney(number, currencyCode);
  }

  /**
   * Static factory method for creating a new instance of {@link JPMoney}.
   *
   * @param number The numeric part, not null.
   * @return A new instance of {@link Money}.
   */
  public static JPMoney ofRub(BigDecimal number) {
    return of(number, CurrencyCode.RUB);
  }
}
