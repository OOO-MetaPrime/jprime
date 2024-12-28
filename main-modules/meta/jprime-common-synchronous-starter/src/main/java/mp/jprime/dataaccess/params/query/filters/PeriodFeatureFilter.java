package mp.jprime.dataaccess.params.query.filters;

import java.time.LocalDate;

public class PeriodFeatureFilter extends FeatureFilter {
  /**
   * Дата начала периода проверки признака
   */
  private final LocalDate checkFromDay;
  /**
   * Дата окончания периода проверки признака
   */
  private final LocalDate checkToDay;

  /**
   * Конструктор
   *
   * @param featureCode  Кодовое имя признака
   * @param checkFromDay Дата начала периода проверки признака
   * @param checkToDay   Дата окончания периода проверки признака
   */
  public PeriodFeatureFilter(String featureCode, LocalDate checkFromDay, LocalDate checkToDay) {
    super(featureCode);
    this.checkFromDay = checkFromDay;
    this.checkToDay = checkToDay;
  }

  /**
   * Дата начала периода проверки признака
   *
   * @return Дата начала периода проверки признака
   */
  public LocalDate getCheckFromDay() {
    return checkFromDay;
  }

  /**
   * Дата окончания периода проверки признака
   *
   * @return Дата окончания периода проверки признака
   */
  public LocalDate getCheckToDay() {
    return checkToDay;
  }
}
