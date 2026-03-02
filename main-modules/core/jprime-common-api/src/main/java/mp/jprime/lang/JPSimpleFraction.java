package mp.jprime.lang;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;

/**
 * Простая дробь
 */
public final class JPSimpleFraction implements Serializable, Comparable<JPSimpleFraction> {
  /**
   * Признак положительной дроби
   */
  private final boolean positive;
  /**
   * Целая часть дроби
   */
  private final int integer;
  /**
   * Числитель дроби
   */
  private final int numerator;
  /**
   * Знаменатель дроби
   */
  private final int denominator;

  private final double doubleValue;

  /**
   * Конструктор
   *
   * @param integer Целая часть дроби
   */
  public static JPSimpleFraction of(int integer) {
    return new JPSimpleFraction(integer, 0, 1);
  }

  /**
   * Конструктор
   *
   * @param numerator   Числитель дроби
   * @param denominator Знаменатель дроби
   */
  public static JPSimpleFraction of(int numerator, int denominator) {
    return new JPSimpleFraction(0, numerator, denominator);
  }

  /**
   * Конструктор
   *
   * @param integer     Целая часть дроби
   * @param numerator   Числитель дроби
   * @param denominator Знаменатель дроби
   */
  public static JPSimpleFraction of(int integer, int numerator, int denominator) {
    return new JPSimpleFraction(integer, numerator, denominator);
  }

  /**
   * Конструктор
   *
   * @param positive    Знак дроби
   * @param integer     Целая часть дроби
   * @param numerator   Числитель дроби
   * @param denominator Знаменатель дроби
   */
  public static JPSimpleFraction of(Boolean positive, Integer integer, Integer numerator, Integer denominator) {
    return new JPSimpleFraction(
        (integer != null ? integer : 0),
        (numerator != null ? numerator : 0),
        (positive == null || positive ? 1 : -1) * (denominator != null ? denominator : 1)
    );
  }

  /**
   * Конструктор
   *
   * @param integerValue     Целая часть дроби
   * @param numeratorValue   Числитель дроби
   * @param denominatorValue Знаменатель дроби
   */
  private JPSimpleFraction(int integerValue, int numeratorValue, int denominatorValue) {
    if (denominatorValue == 0) {
      throw new ArithmeticException("Denominator is zero");
    }
    BigInteger biInteger = new BigInteger(String.valueOf(integerValue));
    BigInteger biNumerator = new BigInteger(String.valueOf(numeratorValue));
    BigInteger biDenominator = new BigInteger(String.valueOf(denominatorValue));
    /*
     * Определяем знак дроби
     */
    boolean positive = (1 == ((biInteger.signum() < 0 ? -1 : 1) *
        (biNumerator.signum() < 0 ? -1 : 1) *
        (biDenominator.signum() < 0 ? -1 : 1)));

    /*
     * Получаем целую часть, числитель и знаменатель дроби
     */
    int integer = biInteger.abs().intValue();
    int numerator = biNumerator.abs().intValue();
    int denominator = biDenominator.abs().intValue();

    if (numerator >= denominator) {
      /*
       * Пытаемся увеличить целую часть
       */
      int newNumerator = numerator % denominator; // Новый числитель

      integer = integer + (numerator - newNumerator) / denominator;
      numerator = newNumerator;
    }

    /*
     * Упрощаем дробь:
     * 1. Получаем наибольший общий делитель
     * 2. Сокращаем числитель и знаменатель
     */
    BigInteger biGCD = biNumerator.gcd(biDenominator);
    numerator = numerator / biGCD.intValue();
    denominator = denominator / biGCD.intValue();

    this.positive = positive;
    this.integer = integer;
    this.numerator = numerator;
    this.denominator = denominator;

    this.doubleValue = (double) ((positive ? 1 : -1) * (integer * denominator + numerator)) / denominator;
  }

  /**
   * Возвращает признак положительной дроби
   *
   * @return Да/Нет
   */
  public boolean isPositive() {
    return positive;
  }

  /**
   * Возвращает целую часть дроби
   *
   * @return Целая часть дроби
   */
  public int getInteger() {
    return integer;
  }

  /**
   * Возвращает числитель дроби
   *
   * @return Числитель дроби
   */
  public int getNumerator() {
    return numerator;
  }

  /**
   * Возвращает знаменатель дроби
   *
   * @return Знаменатель дроби
   */
  public int getDenominator() {
    return denominator;
  }

  /**
   * Перепределяем toString()
   *
   * @return toString
   */
  public String toString() {
    return (this.positive ? "" : "-") + // Знак
        (this.integer != 0 || this.denominator == 1 ? this.integer + " " : "") + // Целая часть
        (this.numerator != 0 ? this.numerator + "/" + this.denominator : ""); // Дробь
  }

  /**
   * Копируем объект
   *
   * @return клон объекта
   */
  private JPSimpleFraction copy() {
    return new JPSimpleFraction((this.positive ? 1 : -1) * this.integer, this.numerator, this.denominator);
  }

  /**
   * Складывает текущий объект и указанный
   *
   * @param other Объект для сложения
   * @return Сумма дробей
   */
  public JPSimpleFraction add(JPSimpleFraction other) {
    if (other == null) {
      return this.copy();
    }
    // a b/c + d e/f = ((a*c+b)*f + (d*f+e)*c)/(c*f)
    int sign = this.positive ? 1 : -1;
    int otherSign = other.positive ? 1 : -1;
    int resultNumerator = sign * (this.integer * this.denominator + this.numerator) * other.denominator +
        otherSign * (other.integer * other.denominator + other.numerator) * this.denominator;
    int resultDenominator = this.denominator * other.denominator;
    return of(resultNumerator, resultDenominator);
  }

  /**
   * Вычитает текущий объект и указанный
   *
   * @param other Объект для вычитания
   * @return Разница дробей
   */
  public JPSimpleFraction minus(JPSimpleFraction other) {
    if (other == null) {
      return this.copy();
    }
    // a b/c - d e/f = ((a*c+b)*f - (d*f+e)*c)/(c*f)
    int sign = this.positive ? 1 : -1;
    int otherSign = other.positive ? 1 : -1;
    int resultNumerator = sign * (this.integer * this.denominator + this.numerator) * other.denominator -
        otherSign * (other.integer * other.denominator + other.numerator) * this.denominator;
    int resultDenominator = this.denominator * other.denominator;
    return of(resultNumerator, resultDenominator);
  }

  /**
   * Умножает текущий объект и указанный
   *
   * @param other Объект для умножения
   * @return Разница дробей
   */
  public JPSimpleFraction inc(JPSimpleFraction other) {
    if (other == null) {
      return this.copy();
    }
    // a b/c * d e/f = (a*c + b)*(d*f + e)/c*f
    int sign = this.positive ? 1 : -1;
    int otherSign = other.positive ? 1 : -1;
    int resultNumerator = sign * (this.integer * this.denominator + this.numerator) *
        otherSign * (other.integer * other.denominator + other.numerator);
    int resultDenominator = this.denominator * other.denominator;
    return of(resultNumerator, resultDenominator);
  }

  /**
   * Делит текущий объект и указанный
   *
   * @param other Объект для деления
   * @return Разница дробей
   */
  public JPSimpleFraction div(JPSimpleFraction other) {
    if (other == null) {
      return this.copy();
    }
    // a b/c : d e/f = (a*c + b)*f / c*(d*f+e)
    int sign = this.positive ? 1 : -1;
    int otherSign = other.positive ? 1 : -1;
    int resultNumerator = otherSign * sign * (this.integer * this.denominator + this.numerator) * other.denominator;
    int resultDenominator = (other.integer * other.denominator + other.numerator) * this.denominator;
    return of(resultNumerator, resultDenominator);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    JPSimpleFraction that = (JPSimpleFraction) o;
    return positive == that.positive &&
        integer == that.integer &&
        numerator == that.numerator &&
        denominator == that.denominator;
  }

  @Override
  public int hashCode() {
    return Objects.hash(positive, integer, numerator, denominator);
  }

  @Override
  public int compareTo(JPSimpleFraction o) {
    return Double.compare(this.doubleValue, o.doubleValue);
  }
}
