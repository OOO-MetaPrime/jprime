package mp.jprime.lang;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public final class JPDateRange extends JPRange<LocalDate> {

  private JPDateRange(LocalDate lower, LocalDate upper, int mask) {
    super(lower, upper, mask, LocalDate.class);
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
  @SuppressWarnings("unchecked")
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
  @SuppressWarnings("unchecked")
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
  @SuppressWarnings("unchecked")
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
  @SuppressWarnings("unchecked")
  public static JPDateRange infinite() {
    return new JPDateRange(null, null, LOWER_INFINITE | UPPER_INFINITE);
  }

  /**
   * Создание {@code LocalDate} диапазона из переданных значений
   *
   * @param lower нижняя граница
   * @param upper верхняя граница
   * @return The range of {@code LocalDate}s.
   * @throws DateTimeParseException when one of the bounds are invalid.
   */
  public static JPDateRange create(LocalDate lower, LocalDate upper) {
    int mask = LOWER_INCLUSIVE | UPPER_INCLUSIVE;
    if (lower == null) {
      mask |= LOWER_INFINITE;
    }
    if (upper == null) {
      mask |= UPPER_INFINITE;
    }
    return new JPDateRange(lower, upper, mask);
  }

  public static JPDateRange emptyRange() {
    return new JPDateRange(
        null,
        null,
        LOWER_INFINITE | UPPER_INFINITE
    );
  }
}
