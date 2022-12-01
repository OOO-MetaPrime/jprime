package mp.jprime.time;

import java.time.LocalDate;

/**
 * Описание периода С-По
 */
public final class JPPeriod {
  private LocalDate from;
  private LocalDate to;

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
