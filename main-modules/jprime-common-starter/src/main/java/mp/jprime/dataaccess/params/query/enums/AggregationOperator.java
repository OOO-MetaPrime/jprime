package mp.jprime.dataaccess.params.query.enums;

/**
 * Агрегации
 */
public enum AggregationOperator {
  /**
   * MAX
   */
  MAX("MAX"),
  /**
   * MIN
   */
  MIN("MIN"),
  /**
   * AVG
   */
  AVG("AVG"),
  /**
   * SUM
   */
  SUM("SUM"),
  /**
   * COUNT
   */
  COUNT("COUNT"),
  /**
   * COUNT_DISTINCT
   */
  COUNT_DISTINCT("COUNT_DISTINCT");

  private String code;

  /**
   * Код
   *
   * @return Код
   */
  public String getCode() {
    return code;
  }

  AggregationOperator(String code) {
    this.code = code;
  }

  /**
   * Возвращает условие по коду
   *
   * @param code Код
   * @return Логическое условие
   */
  public static AggregationOperator getOperator(String code) {
    if (code == null) {
      return null;
    }
    for (AggregationOperator v : AggregationOperator.values()) {
      if (v.code.equalsIgnoreCase(code)) {
        return v;
      }
    }
    return null;
  }
}
