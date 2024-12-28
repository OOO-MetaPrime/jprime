package mp.jprime.dataaccess.params.query.filters;

import mp.jprime.dataaccess.params.query.Filter;

/**
 * Условие признаков
 */
public class FeatureFilter extends Filter {
  /**
   * Кодовое имя признака
   */
  private final String featureCode;

  /**
   * Конструктор
   *
   * @param featureCode Кодовое имя признака
   */
  public FeatureFilter(String featureCode) {
    this.featureCode = featureCode;
  }

  /**
   * Кодовое имя признака
   *
   * @return Кодовое имя признака
   */
  public String getFeatureCode() {
    return featureCode;
  }
}
