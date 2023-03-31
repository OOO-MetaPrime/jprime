package mp.jprime.time;

import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Набор утилитарных методов методов для работы с {@link JPPeriod JPPeriod}
 */
public class JPPeriodUtils {

  /**
   * Объединение периодов
   *
   * @param periods массив {@link JPPeriod периодов} для объединения
   * @return {@link Collection коллеция} {@link JPPeriod}
   */
  public static Collection<JPPeriod> add(JPPeriod... periods) {
    if (periods == null) {
      return Collections.emptyList();
    }
    return add(Arrays.asList(periods));
  }

  /**
   * Объединение периодов
   *
   * @param periods {@link Collection коллеция} {@link JPPeriod периодов} для объединения
   * @return {@link Collection коллеция} {@link JPPeriod}
   */
  public static Collection<JPPeriod> add(Collection<JPPeriod> periods) {
    if (CollectionUtils.isEmpty(periods)) {
      return Collections.emptyList();
    }
    JPPeriods period = JPPeriods.get();
    periods.forEach(period::add);
    return period.getPeriod();
  }

  /**
   * Поиск единого пересечения всех переданных периодов. Значение NULL для даты начала или окончания периода учитывает как бесконечность.
   * При передаче пустого массива периодов, вернет null
   *
   * @param periods - массив периодов
   * @return {@link JPPeriod}
   */
  public static JPPeriod intersectionPeriod(JPPeriod... periods) {
    if (periods == null) {
      return null;
    }

    return intersectionPeriod(Arrays.asList(periods));
  }

  /**
   * Поиск единого пересечения всех переданных периодов. Значение NULL для даты начала или окончания периода учитывает как бесконечность.
   * При передаче пустой коллекции периодов или коллекции состоящей из null элементов, вернет null
   *
   * @param periods - коллекция периодов
   * @return {@link JPPeriod}
   */
  public static JPPeriod intersectionPeriod(Collection<JPPeriod> periods) {
    if (CollectionUtils.isEmpty(periods)) {
      return null;
    }

    LocalDate beginDate = null;
    LocalDate endDate = null;
    boolean isNullOnly = true;

    for (JPPeriod p : periods) {
      if (p == null) {
        continue;
      }

      isNullOnly = false;
      beginDate = max(beginDate, p.getFrom());
      endDate = min(endDate, p.getTo());
    }

    if (isNullOnly) {
      return null;
    }
    if (beginDate == null || endDate == null) {
      return JPPeriod.get(beginDate, endDate);
    }

    return !beginDate.isAfter(endDate) ? JPPeriod.get(beginDate, endDate) : null;
  }

  private static LocalDate min(LocalDate oldDate, LocalDate newDate) {
    if (oldDate == null) {
      return newDate;
    }
    return newDate == null ? oldDate : oldDate.isBefore(newDate) ? oldDate : newDate;
  }

  private static LocalDate max(LocalDate oldDate, LocalDate newDate) {
    if (oldDate == null) {
      return newDate;
    }
    return newDate == null ? oldDate : oldDate.isAfter(newDate) ? oldDate : newDate;
  }
}
