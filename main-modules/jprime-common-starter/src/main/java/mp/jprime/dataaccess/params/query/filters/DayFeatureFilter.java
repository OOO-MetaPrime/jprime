package mp.jprime.dataaccess.params.query.filters;

import java.time.LocalDate;

public class DayFeatureFilter extends FeatureFilter {
  /**
   * Дата проверки признака
   */
  private final LocalDate checkDay;

  /**
   * Конструктор
   *
   * @param featureCode Кодовое имя признака
   * @param checkDay    День проверки признака
   */
  public DayFeatureFilter(String featureCode, LocalDate checkDay) {
    super(featureCode);
    this.checkDay = checkDay;
  }

  /**
   * День проверки признака
   *
   * @return День проверки признака
   */
  public LocalDate getCheckDay() {
    return checkDay;
  }
}
