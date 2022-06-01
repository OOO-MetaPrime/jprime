package mp.jprime.lang;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public final class JPDateTimeRange extends JPRange<LocalDateTime> {

  private JPDateTimeRange(LocalDateTime lower, LocalDateTime upper, int mask) {
    super(lower, upper, mask, LocalDateTime.class);
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
  public static JPDateTimeRange closed(LocalDateTime lower, LocalDateTime upper) {
    Objects.requireNonNull(lower);
    Objects.requireNonNull(upper);
    return new JPDateTimeRange(lower, upper, LOWER_INCLUSIVE | UPPER_INCLUSIVE);
  }

  /**
   * Создание с двух сторон открытого диапазона.
   *
   * <pre>{@code
   *     (a, b) = {x | a < x < b}
   * }</pre>
   *
   * @param lower The lower bound, never null.
   * @param upper The upper bound, never null.
   * @return The range.
   */
  @SuppressWarnings("unchecked")
  public static JPDateTimeRange open(LocalDateTime lower, LocalDateTime upper) {
    Objects.requireNonNull(lower);
    Objects.requireNonNull(upper);
    return new JPDateTimeRange(lower, upper, LOWER_EXCLUSIVE | UPPER_EXCLUSIVE);
  }

  /**
   * Создание открытого слева и закрытого справа диапазона.
   *
   * <pre>{@code
   *     (a, b] = {x | a < x <= b}
   * }</pre>
   *
   * @param lower The lower bound, never null.
   * @param upper The upper bound, never null.
   * @return The range.
   */
  @SuppressWarnings("unchecked")
  public static JPDateTimeRange openClosed(LocalDateTime lower, LocalDateTime upper) {
    Objects.requireNonNull(lower);
    Objects.requireNonNull(upper);
    return new JPDateTimeRange(lower, upper, LOWER_EXCLUSIVE | UPPER_INCLUSIVE);
  }

  /**
   * Создание закрытого слева и открытого справа диапазона.
   *
   * <pre>{@code
   *     [a, b) = {x | a <= x < b}
   * }</pre>
   *
   * @param lower The lower bound, never null.
   * @param upper The upper bound, never null.
   * @return The range.
   */
  @SuppressWarnings("unchecked")
  public static JPDateTimeRange closedOpen(LocalDateTime lower, LocalDateTime upper) {
    Objects.requireNonNull(lower);
    Objects.requireNonNull(upper);
    return new JPDateTimeRange(lower, upper, LOWER_INCLUSIVE | UPPER_EXCLUSIVE);
  }

  /**
   * Создание слева открытого, справа неограниченного диапазона.
   *
   * <pre>{@code
   *     (a, +∞) = {x | x > a}
   * }</pre>
   *
   * @param lower The lower bound, never null.
   * @return The range.
   */
  @SuppressWarnings("unchecked")
  public static JPDateTimeRange openInfinite(LocalDateTime lower) {
    Objects.requireNonNull(lower);
    return new JPDateTimeRange(lower, null, LOWER_EXCLUSIVE | UPPER_INFINITE);
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
  public static JPDateTimeRange closedInfinite(LocalDateTime lower) {
    Objects.requireNonNull(lower);
    return new JPDateTimeRange(lower, null, LOWER_INCLUSIVE | UPPER_INFINITE);
  }

  /**
   * Создание слева неограниченного, справа открытого диапазона.
   *
   * <pre>{@code
   *     (-∞, b) = {x | x < b}
   * }</pre>
   *
   * @param upper The upper bound, never null.
   * @return The range.
   */
  @SuppressWarnings("unchecked")
  public static JPDateTimeRange infiniteOpen(LocalDateTime upper) {
    Objects.requireNonNull(upper);
    return new JPDateTimeRange(null, upper, UPPER_EXCLUSIVE | LOWER_INFINITE);
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
  public static JPDateTimeRange infiniteClosed(LocalDateTime upper) {
    Objects.requireNonNull(upper);
    return new JPDateTimeRange(null, upper, UPPER_INCLUSIVE | LOWER_INFINITE);
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
  public static JPDateTimeRange infinite() {
    return new JPDateTimeRange(null, null, LOWER_INFINITE | UPPER_INFINITE);
  }

  /**
   * Создание {@code LocalDateTime} диапазона из переданных значений
   *
   * @param lower      нижняя граница
   * @param upper      верхняя граница
   * @param lowerClose - признак вхождения нижней границы
   * @param upperClose - признак вхождения верхней границы
   * @return The range of {@code LocalDateTime}s.
   * @throws DateTimeParseException when one of the bounds are invalid.
   */
  public static JPDateTimeRange create(LocalDateTime lower, LocalDateTime upper, boolean lowerClose, boolean upperClose) {
    int mask = lowerClose ? LOWER_INCLUSIVE : LOWER_EXCLUSIVE;
    mask |= upperClose ? UPPER_INCLUSIVE : UPPER_EXCLUSIVE;
    if (lower == null) {
      mask |= LOWER_INFINITE;
    }
    if (upper == null) {
      mask |= UPPER_INFINITE;
    }
    return new JPDateTimeRange(lower, upper, mask);
  }

  public static JPDateTimeRange emptyRange() {
    return new JPDateTimeRange(
        null,
        null,
        LOWER_INFINITE | UPPER_INFINITE
    );
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append(hasMask(LOWER_INCLUSIVE) ? '[' : '(')
        .append(hasLowerBound() ? lower.toString() : "")
        .append(",")
        .append(hasUpperBound() ? upper.toString() : "")
        .append(hasMask(UPPER_INCLUSIVE) ? ']' : ')');

    return sb.toString();
  }
}
