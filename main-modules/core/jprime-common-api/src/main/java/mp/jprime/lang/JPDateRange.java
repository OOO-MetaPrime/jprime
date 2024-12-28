package mp.jprime.lang;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public final class JPDateRange extends JPRange<LocalDate> {

  private JPDateRange(LocalDate lower, LocalDate upper, int mask) {
    super(lower, upper, mask, LocalDate.class);
  }


  /**
   * Returns the lower bound of this range. If {@code null} is returned then this range is left-unbounded.
   *
   * @return The lower bound.
   */
  public LocalDate getFromDate() {
    return lower();
  }

  /**
   * Returns the upper bound of this range. If {@code null} is returned then this range is right-unbounded.
   *
   * @return The upper bound.
   */
  public LocalDate getToDate() {
    return upper;
  }


  /**
   * Создание закрытого c двух сторон диапазона.
   *
   * <pre>{@code
   *   [a, b] = {x | a <= x <= b}
   * }</pre>.
   *
   * @param lower The lower bound, never null.
   * @param upper The upper bound, never null.
   * @return The closed range.
   */
  public static JPDateRange closed(LocalDate lower, LocalDate upper) {
    Objects.requireNonNull(lower);
    Objects.requireNonNull(upper);
    return new JPDateRange(lower, upper, LOWER_INCLUSIVE | UPPER_INCLUSIVE);
  }

  /**
   * Создание слева закрытого, справа неограниченного диапазона
   *
   * <pre>{@code
   *     [a, +∞) = {x | x >= a}
   * }</pre>
   *
   * @param lower The lower bound, never null.
   * @return The range.
   */
  public static JPDateRange closedInfinite(LocalDate lower) {
    Objects.requireNonNull(lower);
    return new JPDateRange(lower, null, LOWER_INCLUSIVE | UPPER_INFINITE);
  }

  /**
   * Создание слева неограниченного, справа закрытого диапазона.
   *
   * <pre>{@code
   *     (-∞, b] = {x | x =< b}
   * }</pre>
   *
   * @param upper The upper bound, never null.
   * @return The range.
   */
  public static JPDateRange infiniteClosed(LocalDate upper) {
    Objects.requireNonNull(upper);
    return new JPDateRange(null, upper, UPPER_INCLUSIVE | LOWER_INFINITE);
  }

  /**
   * Создание с двух сторон неограниченного диапазона.
   *
   * <pre>{@code
   *     (-∞, +∞) = ℝ
   * }</pre>
   *
   * @return The infinite range.
   */
  public static JPDateRange infinite() {
    return new JPDateRange(null, null, LOWER_INFINITE | UPPER_INFINITE);
  }

  /**
   * Создание закрытого для not null значений {@code LocalDate} диапазона
   *
   * @param lower нижняя граница
   * @param upper верхняя граница
   * @return The range of {@code LocalDate}s.
   * @throws DateTimeParseException when one of the bounds are invalid.
   */
  public static JPDateRange create(LocalDate lower, LocalDate upper) {
    return create(lower, upper, true, true);
  }

  /**
   * Создание {@code LocalDate} диапазона из переданных значений
   *
   * @param lower нижняя граница
   * @param upper верхняя граница
   * @param lowerClosed нижняя граница закрыта
   * @param upperClosed верхняя граница закрыта
   * @return The range of {@code LocalDate}s.
   * @throws DateTimeParseException when one of the bounds are invalid.
   */
  public static JPDateRange create(LocalDate lower, LocalDate upper, boolean lowerClosed, boolean upperClosed) {
    int mask = lowerClosed ? LOWER_INCLUSIVE : LOWER_EXCLUSIVE;
    mask |= upperClosed ? UPPER_INCLUSIVE : UPPER_EXCLUSIVE;
    if (lower == null) {
      mask |= LOWER_INFINITE;
    }
    if (upper == null) {
      mask |= UPPER_INFINITE;
    }
    return new JPDateRange(lower, upper, mask);
  }

  public static JPDateRange emptyRange() {
    return new JPDateRange(null, null, LOWER_INFINITE | UPPER_INFINITE);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof JPDateRange)) return false;
    JPDateRange range = (JPDateRange) o;
    if (!Objects.equals(clazz, range.clazz)) return false;
    return
        ((this.lower() == null && range.lower() == null) || this.lower() != null && this.lower().equals(range.lower()))
            &&
            ((this.upper() == null && range.upper() == null) || this.upper() != null && this.upper().equals(range.upper()));
  }

  /**
   * Признак пересечения указанного периода с периодом
   *
   * @param value Период
   * @return Да/Нет
   */
  public boolean intersection(JPDateRange value) {
    if (value == null) {
      return false;
    }

    LocalDate start = value.lower() == null ? LocalDate.MIN : value.lower();
    LocalDate end = value.upper() == null ? LocalDate.MAX : value.upper();

    LocalDate from = lower();
    LocalDate to = upper();

    return (from == null || !from.isAfter(end)) && (to == null || !to.isBefore(start));
  }
}
