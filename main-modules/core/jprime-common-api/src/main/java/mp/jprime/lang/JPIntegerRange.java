package mp.jprime.lang;

import java.util.Objects;

public final class JPIntegerRange extends JPRange<Integer> {

  private JPIntegerRange(Integer lower, Integer upper, int mask) {
    super(lower, upper, mask, Integer.class);
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
  public static JPIntegerRange closed(Integer lower, Integer upper) {
    Objects.requireNonNull(lower);
    Objects.requireNonNull(upper);
    return new JPIntegerRange(lower, upper, LOWER_INCLUSIVE | UPPER_INCLUSIVE);
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
  public static JPIntegerRange closedInfinite(Integer lower) {
    Objects.requireNonNull(lower);
    return new JPIntegerRange(lower, null, LOWER_INCLUSIVE | UPPER_INFINITE);
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
  public static JPIntegerRange infiniteClosed(Integer upper) {
    Objects.requireNonNull(upper);
    return new JPIntegerRange(null, upper, UPPER_INCLUSIVE | LOWER_INFINITE);
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
  public static JPIntegerRange infinite() {
    return new JPIntegerRange(null, null, LOWER_INFINITE | UPPER_INFINITE);
  }

  /**
   * Создание {@code Integer} диапазона из переданных значений
   *
   * @param lower нижняя граница
   * @param upper верхняя граница
   * @return The range of {@code Integer}s.
   * @throws NumberFormatException when one of the bounds are invalid.
   */
  public static JPIntegerRange create(Integer lower, Integer upper) {
    int mask = LOWER_INCLUSIVE | UPPER_INCLUSIVE;
    if (lower == null) {
      mask |= LOWER_INFINITE;
    }
    if (upper == null) {
      mask |= UPPER_INFINITE;
    }
    return new JPIntegerRange(lower, upper, mask);
  }

  public static JPIntegerRange emptyRange() {
    return new JPIntegerRange(
        null,
        null,
        LOWER_INFINITE | UPPER_INFINITE
    );
  }
}
