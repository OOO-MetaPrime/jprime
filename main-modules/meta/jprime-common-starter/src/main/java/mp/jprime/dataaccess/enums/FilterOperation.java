package mp.jprime.dataaccess.enums;

/**
 * Условия выборки
 */
public enum FilterOperation {
  /**
   * Не указано значение
   */
  ISNULL("ISNULL"),
  /**
   * Указано значение
   */
  ISNOTNULL("ISNOTNULL"),
  /**
   * Равно
   */
  EQ("EQ"),
  /**
   * Содержит
   */
  CONTAINS("CONTAINS"),
  /**
   * Не равно
   */
  NEQ("NEQ"),
  /**
   * Больше
   */
  GT("GT"),
  /**
   * Больше или равно
   */
  GTE("GTE"),
  /**
   * Меньше
   */
  LT("LT"),
  /**
   * Меньше или равно
   */
  LTE("LTE"),
  /**
   * Между
   */
  BETWEEN("BETWEEN"),
  /**
   * Начинается С
   */
  STARTSWITH("STARTSWITH"),
  /**
   * Не начинается С
   */
  NOTSTARTSWITH("NOTSTARTSWITH"),
  /**
   * Содержит
   */
  EXISTS("EXISTS"),
  /**
   * Не содержит
   */
  NOTEXISTS("NOTEXISTS"),
  /**
   * Содержит
   */
  LIKE("LIKE"),
  /**
   * Нечеткий поиск
   */
  FUZZYLIKE("FUZZYLIKE"),
  /**
   * Нечеткий поиск c учетом порядка лексем
   */
  FUZZYORDERLIKE("FUZZYORDERLIKE"),
  /**
   * В указанном списке
   */
  IN("IN"),
  /**
   * Не в указанном списке
   */
  NOTIN("NOTIN"),
  /**
   * Равно в годах
   */
  EQ_YEAR("EQ_YEAR"),
  /**
   * Не равно в годах
   */
  NEQ_YEAR("NEQ_YEAR"),
  /**
   * Больше в годах
   */
  GT_YEAR("GT_YEAR"),
  /**
   * Больше или равно в годах
   */
  GTE_YEAR("GTE_YEAR"),
  /**
   * Меньше в годах
   */
  LT_YEAR("LT_YEAR"),
  /**
   * Меньше или равно в годах
   */
  LTE_YEAR("LTE_YEAR"),
  /**
   * Равно в месяцах
   */
  EQ_MONTH("EQ_MONTH"),
  /**
   * Не равно в месяцах
   */
  NEQ_MONTH("NEQ_MONTH"),
  /**
   * Больше в месяцах
   */
  GT_MONTH("GT_MONTH"),
  /**
   * Больше или равно в месяцах
   */
  GTE_MONTH("GTE_MONTH"),
  /**
   * Меньше в месяцах
   */
  LT_MONTH("LT_MONTH"),
  /**
   * Меньше или равно в месяцах
   */
  LTE_MONTH("LTE_MONTH"),
  /**
   * Равно в днях
   */
  EQ_DAY("EQ_DAY"),
  /**
   * Не равно в днях
   */
  NEQ_DAY("NEQ_DAY"),
  /**
   * Больше в днях
   */
  GT_DAY("GT_DAY"),
  /**
   * Больше или равно в днях
   */
  GTE_DAY("GTE_DAY"),
  /**
   * Меньше в днях
   */
  LT_DAY("LT_DAY"),
  /**
   * Меньше или равно в днях
   */
  LTE_DAY("LTE_DAY"),
  /**
   * Этот диапазон содержит элемент
   */
  CONTAINSEL("CONTAINSEL"),
  /**
   * Этот диапазон содержит диапазон
   */
  CONTAINSRANGE("CONTAINSRANGE"),
  /**
   * Этот диапазон содержится в диапазон
   */
  OVERLAPSRANGE("OVERLAPSRANGE"),
  /**
   * Равно
   */
  EQRANGE("EQRANGE"),
  /**
   * Не равно
   */
  NEQRANGE("NEQRANGE"),
  /**
   * Больше
   */
  GTRANGE("GTRANGE"),
  /**
   * Больше или равно
   */
  GTERANGE("GTERANGE"),
  /**
   * Меньше
   */
  LTRANGE("LTRANGE"),
  /**
   * Меньше или равно
   */
  LTERANGE("LTERANGE");

  private String code;

  FilterOperation(String code) {
    this.code = code;
  }

  /**
   * Код условия
   *
   * @return Код условия
   */
  public String getCode() {
    return code;
  }

  /**
   * Возвращает условие по коду
   *
   * @param code Код
   * @return Условия выборки
   */
  public static FilterOperation getOperation(String code) {
    if (code == null) {
      return null;
    }
    for (FilterOperation v : FilterOperation.values()) {
      if (v.code.equalsIgnoreCase(code)) {
        return v;
      }
    }
    return null;
  }
}
