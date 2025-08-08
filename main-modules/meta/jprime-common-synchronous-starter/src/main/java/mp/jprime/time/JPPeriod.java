package mp.jprime.time;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Описание периода С-По
 */
public final class JPPeriod {
  private final LocalDate from;
  private final LocalDate to;

  /**
   * Конструктор
   *
   * @param from Дата С
   * @param to   Дата По
   */
  private JPPeriod(LocalDate from, LocalDate to) {
    this.from = from;
    this.to = to;
  }

  /**
   * Создает Period
   *
   * @param from Дата С
   * @param to   Дата По
   */
  public static JPPeriod get(LocalDate from, LocalDate to) {
    return new JPPeriod(from, to);
  }

  /**
   * Признак нахождения указанной даты в периоде
   *
   * @param value Дата
   * @return Да/Нет
   */
  public boolean contains(LocalDate value) {
    if (value == null) {
      return false;
    }
    return (from == null || !from.isAfter(value)) && (to == null || !to.isBefore(value));
  }

  /**
   * Признак вхождения указанного периода в текущий период (включительно)
   *
   * @param period Период
   * @return Да/Нет
   */
  public boolean contains(JPPeriod period) {
    if (period == null) {
      return false;
    }
    LocalDate inFrom = period.getFrom();
    LocalDate inTo = period.getTo();
    return (from == null || inFrom == null || !from.isAfter(inFrom))
        && (to == null || inTo == null || !to.isBefore(inTo));
  }

  /**
   * Признак пересечения указанного периода с периодом
   *
   * @param value Период
   * @return Да/Нет
   */
  public boolean intersection(JPPeriod value) {
    if (value == null) {
      return false;
    }

    LocalDate start = value.from == null ? LocalDate.MIN : value.from;
    LocalDate end = value.to == null ? LocalDate.MAX : value.to;

    return ((from == null || !from.isAfter(start)) && (to == null || !to.isBefore(start)))
        || ((from == null || !from.isAfter(end)) && (to == null || !to.isBefore(end)));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    JPPeriod jpPeriod = (JPPeriod) o;

    if (!Objects.equals(from, jpPeriod.from)) {
      return false;
    }
    return Objects.equals(to, jpPeriod.to);
  }

  @Override
  public int hashCode() {
    int result = from != null ? from.hashCode() : 0;
    result = 31 * result + (to != null ? to.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return from + ".." + to;
  }

  /**
   * Дата С
   *
   * @return Дата С
   */
  public LocalDate getFrom() {
    return from;
  }

  /**
   * Дата По
   *
   * @return Дата По
   */
  public LocalDate getTo() {
    return to;
  }
}
