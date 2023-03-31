package mp.jprime.time;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * Логика объединения периодов
 */
public final class JPPeriods {
  private Map<LocalDate, JPPeriod> checkDates = new TreeMap<>();

  /**
   * Конструктор
   */
  private JPPeriods() {
  }

  /**
   * Создает Periods
   */
  public static JPPeriods get() {
    return new JPPeriods();
  }

  /**
   * Возвращает список периодов
   *
   * @return Список периодов
   */
  public Collection<JPPeriod> getPeriod() {
    return checkDates.values();
  }

  public JPPeriods add(JPPeriod newPeriod) {
    Map<LocalDate, JPPeriod> newCheckDates = new TreeMap<>();
    JPPeriod oldPeriod = checkDates.get(newPeriod.getFrom());
    if (oldPeriod == null || newPeriod.getTo() == null ||
        (oldPeriod.getTo() != null && oldPeriod.getTo().isBefore(newPeriod.getTo()))) {
      checkDates.put(newPeriod.getFrom(), newPeriod);
    }

    JPPeriod prevPeriod = null;
    for (JPPeriod period : checkDates.values()) {
      if (newCheckDates.isEmpty()) {
        newCheckDates.put(period.getFrom(), period);
      } else if (prevPeriod.getTo() == null) {
        continue;
      } else if (prevPeriod.getTo().isBefore(period.getFrom())) {
        newCheckDates.put(period.getFrom(), period);
      } else if (period.getTo() == null || period.getTo().isAfter(prevPeriod.getTo())) {
        newCheckDates.put(prevPeriod.getFrom(), JPPeriod.get(prevPeriod.getFrom(), period.getTo()));
      }
      prevPeriod = period;
    }
    checkDates = newCheckDates;
    return this;
  }
}
