package mp.jprime.time;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.time.LocalDate;
import java.util.*;

/**
 * Набор утилитарных методов для работы с {@link JPPeriod JPPeriod}
 */
public class JPPeriodUtils {
  private static final String START = "start";
  private static final String END = "end";
  private static final String END_START = END + START;


  /**
   * Объединение периодов
   *
   * @param periods массив {@link JPPeriod периодов} для объединения
   * @return {@link Collection коллекция} {@link JPPeriod}
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
   * @param periods {@link Collection коллекция} {@link JPPeriod периодов} для объединения
   * @return {@link Collection коллекция} {@link JPPeriod}
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

  /**
   * Пересечение групп периодов
   *
   * @param periods группы периодов
   * @return Периоды, в которых пересекаются все группы периодов
   */
  public static Collection<JPPeriod> intersections(JPPeriods... periods) {
    if (ArrayUtils.isEmpty(periods)) {
      return Collections.emptyList();
    }
    return intersections(Arrays.asList(periods));
  }

  /**
   * Пересечение групп периодов
   *
   * @param periods группы периодов
   * @return Периоды, в которых пересекаются все группы периодов
   */
  public static Collection<JPPeriod> intersections(Collection<JPPeriods> periods) {
    if (CollectionUtils.isEmpty(periods)) {
      return Collections.emptyList();
    }
    Iterator<JPPeriods> iterator = periods.iterator();
    JPPeriods first = iterator.next();
    if (periods.size() == 1) {
      return first.getPeriod();
    }
    while (iterator.hasNext()) {
      JPPeriods second = iterator.next();
      JPPeriods intersected = JPPeriods.get();
      first.getPeriod().stream()
          .flatMap(p1 -> second.getPeriod().stream()
              .map(p2 -> intersectionPeriod(p1, p2))
          )
          .filter(Objects::nonNull)
          .forEach(intersected::add);
      if (intersected.isEmpty()) {
        return Collections.emptyList();
      }
      first = intersected;
    }
    return first.getPeriod();
  }

  /**
   * Разбиение периодов
   * <p>
   * Пример:
   * на вход передаются периоды: <p>
   * {@code [
   * {null-01.01.2020},
   * {01.01.2020-10.01.2020},
   * {05.01.2020-07.01.2020},
   * {09.01.2020-20.01.2020},
   * {15.01.2020-25.01.2020},
   * {30.01.2020-null}
   * ]}
   * <p>
   * Результат: <p>
   * {@code [
   * {null-31.12.2019},
   * {01.01.2020-01.01.2020},
   * {02.01.2020-04.01.2020},
   * {05.01.2020-07.01.2020},
   * {08.01.2020-08.01.2020},
   * {09.01.2020-10.01.2020},
   * {11.01.2020-14.01.2020},
   * {15.01.2020-20.01.2020},
   * {21.01.2020-25.01.2020},
   * {30.01.2020-null}
   * ]}
   *
   * @param inputPeriods входящие периоды
   * @return Разбиение периодов
   */
  public static Collection<JPPeriod> split(Collection<JPPeriod> inputPeriods) {
    if (CollectionUtils.isEmpty(inputPeriods)) {
      return Collections.emptyList();
    }
    Map<LocalDate, String> borderDates = new TreeMap<>();
    inputPeriods.forEach(fact -> computeBorders(fact.getFrom(), fact.getTo(), borderDates));

    if (borderDates.isEmpty()) {
      return Collections.singleton(JPPeriod.get(null, null));
    }

    Collection<JPPeriod> periods = add(inputPeriods);

    Iterator<LocalDate> iterator = borderDates.keySet().iterator();
    LocalDate next = iterator.next();
    int i = 0;
    while (i < borderDates.size()) {
      String sign = borderDates.getOrDefault(next, START);
      LocalDate splitDate;
      if (END.equals(sign)) {
        splitDate = next.plusDays(1);
        next = splitDate;
      } else if (START.equals(sign)) {
        splitDate = next;
        i++;
        next = iterator.hasNext() ? iterator.next() : next;
      } else {
        splitDate = next;
        next = next.plusDays(1);
      }
      periods = split(periods, splitDate);
    }

    return periods;
  }

  /**
   * Вычитание периодов
   *
   * @param minuend     периоды, из которых надо вычитать
   * @param subtractant вычитаемые периоды
   * @return результат вычитания периодов
   */
  public static Collection<JPPeriod> subtract(Collection<JPPeriod> minuend, Collection<JPPeriod> subtractant) {
    if (CollectionUtils.isEmpty(minuend)) {
      return Collections.emptyList();
    }

    if (CollectionUtils.isEmpty(subtractant)) {
      return minuend;
    }
    return subtract(JPPeriods.get(minuend), JPPeriods.get(subtractant));
  }

  /**
   * Вычитание периодов
   *
   * @param minuend     периоды, из которых надо вычитать
   * @param subtractant вычитаемые периоды
   * @return результат вычитания периодов
   */
  public static Collection<JPPeriod> subtract(JPPeriods minuend, JPPeriods subtractant) {
    if (minuend == null || minuend.isEmpty()) {
      return Collections.emptyList();
    }

    if (subtractant == null || subtractant.isEmpty()) {
      return minuend.getPeriod();
    }
    Collection<JPPeriod> normalizedSubtractant = subtractant.getPeriod();
    if (normalizedSubtractant.size() == 1) {
      JPPeriod subtractantPeriod = normalizedSubtractant.iterator().next();
      if (subtractantPeriod.getFrom() == null && subtractantPeriod.getTo() == null) {
        return Collections.emptyList();
      }
    }
    Collection<JPPeriod> normalizedMinuend = minuend.getPeriod();

    Collection<JPPeriod> result = new ArrayList<>();

    for (JPPeriod minuendNext : normalizedMinuend) {
      Collection<JPPeriod> currentMinuendPeriods = Collections.singleton(minuendNext);
      for (JPPeriod subtractantNext : normalizedSubtractant) {
        currentMinuendPeriods = currentMinuendPeriods.stream()
            .flatMap(x -> JPPeriodUtils.subtract(x, subtractantNext).stream())
            .toList();
      }
      result.addAll(currentMinuendPeriods);
    }
    return result;
  }

  private static Collection<JPPeriod> subtract(JPPeriod minuend, JPPeriod subtractant) {
    if (minuend == null) {
      return Collections.emptyList();
    }

    if (subtractant == null) {
      return Collections.singleton(minuend);
    }

    LocalDate subtractantFrom = subtractant.getFrom();
    LocalDate subtractantTo = subtractant.getTo();
    LocalDate minuendFrom = minuend.getFrom();
    LocalDate minuendTo = minuend.getTo();

    //Если вычитаемый период перекрывает уменьшаемый
    if ((subtractantFrom == null || (minuendFrom != null && !subtractantFrom.isAfter(minuendFrom)))
        && (subtractantTo == null || (minuendTo != null && !subtractantTo.isBefore(minuendTo)))
    ) {
      return Collections.emptyList();
    }

    //Если вычитаемый период не пересекается с уменьшаемым
    if (subtractantFrom != null && minuendTo != null && minuendTo.isBefore(subtractantFrom)
        || subtractantTo != null && minuendFrom != null && minuendFrom.isAfter(subtractantTo)
    ) {
      return Collections.singleton(minuend);
    }

    Collection<JPPeriod> result = new ArrayList<>();

    if (subtractantFrom != null && (minuendFrom == null || subtractantFrom.isAfter(minuendFrom))) {
      result.add(JPPeriod.get(minuendFrom, subtractantFrom.minusDays(1)));
    }

    if (subtractantTo != null && (minuendTo == null || subtractantTo.isBefore(minuendTo))) {
      result.add(JPPeriod.get(subtractantTo.plusDays(1), minuendTo));
    }
    return result;
  }

  /**
   * Разбиение периодов по указанной дате
   * <p>
   * Все периоды, включающие указанную дату, разбиваются на <p>{@code {from..(splitDate - 1)}, {splitDate..to}}
   * <p>(указанная дата включается во второй период)
   *
   * @param periods   периоды
   * @param splitDate дата разбиения
   * @return Периоды
   */
  private static Collection<JPPeriod> split(Collection<JPPeriod> periods, LocalDate splitDate) {
    if (splitDate == null || CollectionUtils.isEmpty(periods)) {
      return periods;
    }
    Collection<JPPeriod> result = new ArrayList<>();
    for (JPPeriod period : periods) {
      if (splitDate.equals(period.getFrom()) || !period.contains(splitDate)) {
        result.add(period);
      } else {
        result.add(JPPeriod.get(period.getFrom(), splitDate.minusDays(1)));
        result.add(JPPeriod.get(splitDate, period.getTo()));
      }
    }
    return result;
  }

  private static void computeBorders(LocalDate startDate, LocalDate endDate, Map<LocalDate, String> borderDates) {
    if (startDate != null) {
      String found = borderDates.get(startDate);
      if (END.equals(found)) {
        borderDates.put(startDate, END_START);
      } else if (found == null) {
        borderDates.put(startDate, START);
      }
    }
    if (endDate != null) {
      String found = borderDates.get(endDate);
      if (START.equals(found)) {
        borderDates.put(endDate, END_START);
      } else if (found == null) {
        borderDates.put(endDate, END);
      }
    }
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
