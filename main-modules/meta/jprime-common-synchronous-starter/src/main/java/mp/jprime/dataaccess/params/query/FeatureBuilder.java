package mp.jprime.dataaccess.params.query;

import mp.jprime.dataaccess.params.query.filters.DayFeatureFilter;
import mp.jprime.dataaccess.params.query.filters.FeatureFilter;
import mp.jprime.dataaccess.params.query.filters.PeriodFeatureFilter;

import java.time.LocalDate;

public final class FeatureBuilder {
  private final String featureCode; // Кодовое имя признака

  /**
   * Кодовое имя признака
   *
   * @param featureCode Кодовое имя признака
   */
  private FeatureBuilder(String featureCode) {
    this.featureCode = featureCode;
  }

  /**
   * Создаем  FeatureBuilder
   *
   * @param featureCode Кодовое имя признака
   * @return AttrValueBuilder
   */
  public static FeatureBuilder of(String featureCode) {
    return new FeatureBuilder(featureCode);
  }


  /**
   * Проверка признака
   *
   * @return Условие
   */
  public FeatureFilter check() {
    return new FeatureFilter(featureCode);
  }

  /**
   * Проверка признака
   *
   * @param day День, на которую проверяем
   * @return Условие
   */
  public DayFeatureFilter check(LocalDate day) {
    return new DayFeatureFilter(featureCode, day);
  }

  /**
   * Проверка признака
   *
   * @param fromDay Дата начала периода проверки признака
   * @param toDay   Дата окончания периода проверки признака
   * @return Условие
   */
  public PeriodFeatureFilter check(LocalDate fromDay, LocalDate toDay) {
    return new PeriodFeatureFilter(featureCode, fromDay, toDay);
  }
}
