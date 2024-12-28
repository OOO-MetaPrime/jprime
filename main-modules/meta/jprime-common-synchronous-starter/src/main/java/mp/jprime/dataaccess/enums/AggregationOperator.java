package mp.jprime.dataaccess.enums;

import java.util.HashMap;
import java.util.Map;

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

  private static final Map<String, AggregationOperator> BY_CODE = new HashMap<>();

  static {
    for (AggregationOperator type : AggregationOperator.values()) {
      BY_CODE.put(type.getCode().toLowerCase(), type);
    }
  }

  private final String code;

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
    return code == null ? null : BY_CODE.get(code.toLowerCase());
  }
}
