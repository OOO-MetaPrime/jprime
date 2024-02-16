package mp.jprime.utils;

import mp.jprime.lang.JPTimeRange;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalTime;
import java.time.temporal.TemporalUnit;
import java.util.*;

/**
 * Набор утилитарных методов для работы с {@link JPTimeRange периодами времени}
 */
public abstract class JPTimeRangeUtils {

  /**
   * Объединение {@link JPTimeRange периодов времени}
   *
   * @param ranges   периоды времени
   * @param accuracy точность
   * @return Список объединенных {@link JPTimeRange периодов времени}
   */
  public static Collection<JPTimeRange> add(Collection<JPTimeRange> ranges, TemporalUnit accuracy) {
    if (CollectionUtils.isEmpty(ranges)) {
      return Collections.emptyList();
    }
    Map<LocalTime, JPTimeRange> rangeMap = new TreeMap<>(Comparator.nullsFirst(Comparator.naturalOrder()));
    for (JPTimeRange newRange : ranges) {
      Map<LocalTime, JPTimeRange> newRangeMap = new TreeMap<>(Comparator.nullsFirst(Comparator.naturalOrder()));
      JPTimeRange oldRange = rangeMap.get(newRange.lower());
      if (oldRange == null || newRange.upper() == null ||
          (oldRange.upper() != null && oldRange.upper().isBefore(newRange.upper()))) {
        rangeMap.put(newRange.lower(), newRange);
      }

      JPTimeRange prevRange = null;
      for (JPTimeRange range : rangeMap.values()) {
        if (range.isInfinite()) {
          return Collections.singleton(JPTimeRange.infinite());
        }
        LocalTime upper = range.upper();
        LocalTime prevUpper = prevRange == null ? null : prevRange.upper();
        if (newRangeMap.isEmpty()) {
          newRangeMap.put(range.lower(), range);
        } else if (prevUpper == null) {
          continue;
        } else if (prevUpper.plus(1, accuracy).isBefore(range.lower())) {
          newRangeMap.put(range.lower(), range);
        } else if (upper == null || !upper.isBefore(prevUpper)) {
          boolean lowerClosed = isLowerClosed(range, prevRange);
          boolean upperClosed = isUpperClosed(range, prevRange);
          LocalTime prevLower = prevRange.lower();
          newRangeMap.put(prevLower, JPTimeRange.create(prevLower, max(upper, prevUpper), lowerClosed, upperClosed));
        }
        prevRange = range;
      }
      rangeMap = newRangeMap;
    }
    return rangeMap.values();
  }

  private static boolean isLowerClosed(JPTimeRange range, JPTimeRange prevRange) {
    LocalTime lower = range.lower();
    LocalTime prevLower = prevRange.lower();
    if (lower == null || prevLower == null) {
      return false;
    }
    if (lower.equals(prevLower)) {
      return range.isLowerBoundClosed() || prevRange.isLowerBoundClosed();
    }
    return prevLower.isBefore(lower) ? prevRange.isLowerBoundClosed() : range.isLowerBoundClosed();
  }

  private static boolean isUpperClosed(JPTimeRange range, JPTimeRange prevRange) {
    LocalTime upper = range.upper();
    LocalTime prevUpper = prevRange.upper();
    if (upper == null || prevUpper == null) {
      return false;
    }
    if (upper.equals(prevUpper)) {
      return range.isUpperBoundClosed() || prevRange.isUpperBoundClosed();
    }
    return prevUpper.isAfter(upper) ? prevRange.isUpperBoundClosed() : range.isUpperBoundClosed();
  }

  private static LocalTime max(LocalTime one, LocalTime two) {
    if (one == null || two == null) {
      return null;
    }
    return one.isAfter(two) ? one : two;
  }
}
