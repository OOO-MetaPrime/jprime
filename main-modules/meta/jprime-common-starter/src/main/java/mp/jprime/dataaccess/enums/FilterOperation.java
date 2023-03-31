package mp.jprime.dataaccess.enums;

/**
 * Условия выборки
 */
public enum FilterOperation {
  /**
   * Между
   */
  BETWEEN("BETWEEN"),
  /**
   * Содержит пару ключ-значение
   */
  CONTAINS_KVP("CONTAINS_KVP"),
  /**
   * Этот диапазон содержит элемент
   */
  CONTAINSELEMENT("CONTAINSELEMENT"),
  /**
   * Этот диапазон содержит диапазон
   */
  CONTAINSRANGE("CONTAINSRANGE"),
  /**
   * Равно
   */
  EQ("EQ"),
  /**
   * Равно
   */
  EQRANGE("EQRANGE"),
  /**
   * Равно в днях
   */
  EQ_DAY("EQ_DAY"),
  /**
   * Равно в месяцах
   */
  EQ_MONTH("EQ_MONTH"),
  /**
   * Равно в годах
   */
  EQ_YEAR("EQ_YEAR"),
  /**
   * Содержит
   */
  EXISTS("EXISTS"),
  /**
   * Нечеткий поиск
   */
  FUZZYLIKE("FUZZYLIKE"),
  /**
   * Нечеткий поиск c учетом порядка лексем
   */
  FUZZYORDERLIKE("FUZZYORDERLIKE"),
  /**
   * Нечеткий поиск(без преобразования)
   */
  FUZZYQUERY("FUZZYQUERY"),
  /**
   * Больше
   */
  GT("GT"),
  /**
   * Больше
   */
  GTRANGE("GTRANGE"),
  /**
   * Больше в днях
   */
  GT_DAY("GT_DAY"),
  /**
   * Больше в месяцах
   */
  GT_MONTH("GT_MONTH"),
  /**
   * Больше в годах
   */
  GT_YEAR("GT_YEAR"),
  /**
   * Больше или равно
   */
  GTE("GTE"),
  /**
   * Больше или равно
   */
  GTERANGE("GTERANGE"),
  /**
   * Больше или равно в днях
   */
  GTE_DAY("GTE_DAY"),
  /**
   * Больше или равно в месяцах
   */
  GTE_MONTH("GTE_MONTH"),
  /**
   * Больше или равно в годах
   */
  GTE_YEAR("GTE_YEAR"),
  /**
   * В указанном списке
   */
  IN("IN"),
  /**
   * Указано значение
   */
  ISNOTNULL("ISNOTNULL"),
  /**
   * Не указано значение
   */
  ISNULL("ISNULL"),
  /**
   * Содержит
   */
  LIKE("LIKE"),
  /**
   * Меньше
   */
  LT("LT"),
  /**
   * Меньше
   */
  LTRANGE("LTRANGE"),
  /**
   * Меньше в днях
   */
  LT_DAY("LT_DAY"),
  /**
   * Меньше в месяцах
   */
  LT_MONTH("LT_MONTH"),
  /**
   * Меньше в годах
   */
  LT_YEAR("LT_YEAR"),
  /**
   * Меньше или равно
   */
  LTE("LTE"),
  /**
   * Меньше или равно
   */
  LTERANGE("LTERANGE"),
  /**
   * Меньше или равно в днях
   */
  LTE_DAY("LTE_DAY"),
  /**
   * Меньше или равно в месяцах
   */
  LTE_MONTH("LTE_MONTH"),
  /**
   * Меньше или равно в годах
   */
  LTE_YEAR("LTE_YEAR"),
  /**
   * Не равно
   */
  NEQ("NEQ"),
  /**
   * Не равно
   */
  NEQRANGE("NEQRANGE"),
  /**
   * Не равно в днях
   */
  NEQ_DAY("NEQ_DAY"),
  /**
   * Не равно в месяцах
   */
  NEQ_MONTH("NEQ_MONTH"),
  /**
   * Не равно в годах
   */
  NEQ_YEAR("NEQ_YEAR"),
  /**
   * Не содержит
   */
  NOTEXISTS("NOTEXISTS"),
  /**
   * Не в указанном списке
   */
  NOTIN("NOTIN"),
  /**
   * Не начинается С
   */
  NOTSTARTSWITH("NOTSTARTSWITH"),
  /**
   * Этот диапазон содержится в диапазон
   */
  OVERLAPSRANGE("OVERLAPSRANGE"),
  /**
   * Начинается С
   */
  STARTSWITH("STARTSWITH");

  private final String code;

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
