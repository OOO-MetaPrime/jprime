package mp.jprime.lang;

import mp.jprime.formats.DateFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Objects;
import java.util.function.Function;

public abstract class JPRange<T extends Comparable> implements Serializable, Comparable<JPRange<T>> {

  public static final int LOWER_INCLUSIVE = 1 << 1;
  public static final int LOWER_EXCLUSIVE = 1 << 2;
  public static final int UPPER_INCLUSIVE = 1 << 3;
  public static final int UPPER_EXCLUSIVE = 1 << 4;
  public static final int LOWER_INFINITE = (1 << 5) | LOWER_EXCLUSIVE;
  public static final int UPPER_INFINITE = (1 << 6) | UPPER_EXCLUSIVE;

  public static final String EMPTY = "empty";

  public static final String INFINITY = "infinity";

  protected static final DateTimeFormatter LOCAL_DATE_TIME = new DateTimeFormatterBuilder()
      .appendPattern("yyyy-MM-dd HH:mm:ss")
      .optionalStart()
      .appendPattern(".")
      .appendFraction(ChronoField.NANO_OF_SECOND, 1, 6, false)
      .optionalEnd()
      .toFormatter();

  protected static final DateTimeFormatter ZONE_DATE_TIME = new DateTimeFormatterBuilder()
      .appendPattern("yyyy-MM-dd HH:mm:ss")
      .optionalStart()
      .appendPattern(".")
      .appendFraction(ChronoField.NANO_OF_SECOND, 1, 6, false)
      .optionalEnd()
      .appendOffset("+HH:mm", "Z")
      .toFormatter();

  protected final T lower;
  protected final T upper;
  protected final int mask;
  protected final Class<T> clazz;

  protected JPRange(T lower, T upper, int mask, Class<T> clazz) {
    this.lower = lower;
    this.upper = upper;
    this.mask = mask;
    this.clazz = clazz;

    if (isBounded() && lower.compareTo(upper) > 0) {
      throw new IllegalArgumentException("The lower bound is greater then upper!");
    }
  }

  protected static Function<String, String> unquote() {
    return s -> {
      if (s.charAt(0) == '\"' && s.charAt(s.length() - 1) == '\"') {
        return s.substring(1, s.length() - 1);
      }

      return s;
    };
  }

  private boolean isBounded() {
    return !hasMask(LOWER_INFINITE) && !hasMask(UPPER_INFINITE);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof JPRange)) return false;
    JPRange<?> range = (JPRange<?>) o;
    if (!Objects.equals(clazz, range.clazz)) return false;
    return this.toNormalizedString().equals(range.toNormalizedString());
  }

  @Override
  public int hashCode() {
    return Objects.hash(lower, upper, mask, clazz);
  }

  @Override
  public int compareTo(JPRange<T> o) {
    if (o == null) {
      return -1;
    }
    if (o.asString() == null) {
      return 1;
    }
    return this.toNormalizedString().equals(o.toNormalizedString()) ? 0 : 1;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append(hasMask(LOWER_INCLUSIVE) ? '[' : '(')
        .append(hasLowerBound() ? boundToString().apply(lower) : "")
        .append(",")
        .append(hasUpperBound() ? boundToString().apply(upper) : "")
        .append(hasMask(UPPER_INCLUSIVE) ? ']' : ')');

    return sb.toString();
  }

  public boolean hasMask(int flag) {
    return (mask & flag) == flag;
  }

  public boolean isLowerBoundClosed() {
    return hasLowerBound() && hasMask(LOWER_INCLUSIVE);
  }

  public boolean isUpperBoundClosed() {
    return hasUpperBound() && hasMask(UPPER_INCLUSIVE);
  }

  public boolean hasLowerBound() {
    return !hasMask(LOWER_INFINITE);
  }

  public boolean hasUpperBound() {
    return !hasMask(UPPER_INFINITE);
  }

  /**
   * Returns the lower bound of this range. If {@code null} is returned then this range is left-unbounded.
   *
   * @return The lower bound.
   */
  public T lower() {
    return lower;
  }

  /**
   * Returns the upper bound of this range. If {@code null} is returned then this range is right-unbounded.
   *
   * @return The upper bound.
   */
  public T upper() {
    return upper;
  }

  /**
   * Determines whether this range contains this point or not.
   * <p>
   * For example:
   * <pre>{@code
   *     assertTrue(integerRange("[1,2]").contains(1))
   *     assertTrue(integerRange("[1,2]").contains(2))
   *     assertTrue(integerRange("[-1,1]").contains(0))
   *     assertTrue(infinity(Integer.class).contains(Integer.MAX_VALUE))
   *     assertTrue(infinity(Integer.class).contains(Integer.MIN_VALUE))
   *
   *     assertFalse(integerRange("(1,2]").contains(1))
   *     assertFalse(integerRange("(1,2]").contains(3))
   *     assertFalse(integerRange("[-1,1]").contains(0))
   * }</pre>
   *
   * @param point The point to check.
   * @return Whether {@code point} in this range or not.
   */
  @SuppressWarnings("unchecked")
  public boolean contains(T point) {
    boolean l = hasLowerBound();
    boolean u = hasUpperBound();

    if (l && u) {
      boolean inLower = hasMask(LOWER_INCLUSIVE) ? lower.compareTo(point) <= 0 : lower.compareTo(point) < 0;
      boolean inUpper = hasMask(UPPER_INCLUSIVE) ? upper.compareTo(point) >= 0 : upper.compareTo(point) > 0;

      return inLower && inUpper;
    } else if (l) {
      return hasMask(LOWER_INCLUSIVE) ? lower.compareTo(point) <= 0 : lower.compareTo(point) < 0;
    } else if (u) {
      return hasMask(UPPER_INCLUSIVE) ? upper.compareTo(point) >= 0 : upper.compareTo(point) > 0;
    }

    // INFINITY
    return true;
  }

  /**
   * Determines whether this range contains this point or not.
   * <p>
   * For example:
   * <pre>{@code
   *     assertTrue(integerRange("[-2,2]").contains(integerRange("[-1,1]")))
   *     assertTrue(integerRange("(,)").contains(integerRange("(,)"))
   *
   *     assertFalse(integerRange("[-2,2)").contains(integerRange("[-1,2]")))
   *     assertFalse(integerRange("(-2,2]").contains(1))
   * }</pre>
   *
   * @param range The range to check.
   * @return Whether {@code range} in this range or not.
   */
  public boolean contains(JPRange<T> range) {
    return (!range.hasLowerBound() || contains(range.lower)) && (!range.hasUpperBound() || contains(range.upper));
  }

  public String asString() {
    StringBuilder sb = new StringBuilder();

    sb.append(hasMask(LOWER_INCLUSIVE) ? '[' : '(')
        .append(hasLowerBound() ? boundToString().apply(lower) : "")
        .append(",")
        .append(hasUpperBound() ? boundToString().apply(upper) : "")
        .append(hasMask(UPPER_INCLUSIVE) ? ']' : ')');

    return sb.toString();
  }

  private Function<T, String> boundToString() {
    return t -> {
      if (clazz.equals(ZonedDateTime.class)) {
        return ZONE_DATE_TIME.format((ZonedDateTime) t);
      }
      if (clazz.equals(LocalDate.class)) {
        return DateFormat.LOCAL_DATE_FORMAT.format((LocalDate) t);
      }
      if (clazz.equals(LocalDateTime.class)) {
        return DateFormat.LOCAL_DATETIME_FORMAT.format((LocalDateTime) t);
      }

      return t.toString();
    };
  }

  public Class<Object> getClazz() {
    return Object.class;
  }

  abstract public JPRange<T> normalized();

  abstract public String toNormalizedString();
}
