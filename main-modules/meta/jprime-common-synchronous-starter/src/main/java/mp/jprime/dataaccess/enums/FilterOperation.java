package mp.jprime.dataaccess.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Условия выборки
 */
public enum FilterOperation {
  /**
   * Между
   */
  BETWEEN("between"),
  /**
   * Содержит пару ключ-значение
   */
  CONTAINS("contains"),
  /**
   * Этот диапазон содержит элемент
   */
  CONTAINS_ELEMENT("containsElement"),
  /**
   * Этот диапазон содержит диапазон
   */
  CONTAINS_RANGE("containsRange"),
  /**
   * Равно
   */
  EQ("eq"),
  /**
   * Равно
   */
  EQ_RANGE("eqRange"),
  /**
   * Равно в днях
   */
  EQ_DAY("eqDay"),
  /**
   * Равно в месяцах
   */
  EQ_MONTH("eqMonth"),
  /**
   * Равно в годах
   */
  EQ_YEAR("eqYear"),
  /**
   * Содержит
   */
  EXISTS("exists"),
  /**
   * Нечеткий поиск
   */
  FUZZY_LIKE("fuzzyLike"),
  /**
   * Нечеткий поиск c учетом порядка лексем
   */
  FUZZY_ORDER_LIKE("fuzzyOrderLike"),
  /**
   * Нечеткий поиск(без преобразования)
   */
  FUZZY_QUERY("fuzzyQuery"),
  /**
   * Больше
   */
  GT("gt"),
  /**
   * Больше
   */
  GT_RANGE("gtRange"),
  /**
   * Больше в днях
   */
  GT_DAY("gtDay"),
  /**
   * Больше в месяцах
   */
  GT_MONTH("gtMonth"),
  /**
   * Больше в годах
   */
  GT_YEAR("gtYear"),
  /**
   * Больше или равно
   */
  GTE("gte"),
  /**
   * Больше или равно
   */
  GTE_RANGE("gteRange"),
  /**
   * Больше или равно в днях
   */
  GTE_DAY("gteDay"),
  /**
   * Больше или равно в месяцах
   */
  GTE_MONTH("gteMonth"),
  /**
   * Больше или равно в годах
   */
  GTE_YEAR("gteYear"),
  /**
   * В указанном списке
   */
  IN("in"),
  /**
   * В указанном подзапросе
   */
  IN_QUERY("inQuery"),
  /**
   * Указано значение
   */
  ISNOTNULL("isNotNull"),
  /**
   * Не указано значение
   */
  ISNULL("isNull"),
  /**
   * Содержит
   */
  LIKE("like"),
  /**
   * Меньше
   */
  LT("lt"),
  /**
   * Меньше
   */
  LT_RANGE("ltRange"),
  /**
   * Меньше в днях
   */
  LT_DAY("ltDay"),
  /**
   * Меньше в месяцах
   */
  LT_MONTH("ltMonth"),
  /**
   * Меньше в годах
   */
  LT_YEAR("ltYear"),
  /**
   * Меньше или равно
   */
  LTE("lte"),
  /**
   * Меньше или равно
   */
  LTE_RANGE("lteRange"),
  /**
   * Меньше или равно в днях
   */
  LTE_DAY("lteDay"),
  /**
   * Меньше или равно в месяцах
   */
  LTE_MONTH("lteMonth"),
  /**
   * Меньше или равно в годах
   */
  LTE_YEAR("lteYear"),
  /**
   * Не равно
   */
  NEQ("neq"),
  /**
   * Не равно
   */
  NEQ_RANGE("neqRange"),
  /**
   * Не равно в днях
   */
  NEQ_DAY("neqDay"),
  /**
   * Не равно в месяцах
   */
  NEQ_MONTH("neqMonth"),
  /**
   * Не равно в годах
   */
  NEQ_YEAR("neqYear"),
  /**
   * Не содержит
   */
  NOT_EXISTS("notExists"),
  /**
   * Не в указанном списке
   */
  NOT_IN("notIn"),
  /**
   * Не в указанном подзапросе
   */
  NOT_IN_QUERY("notInQuery"),
  /**
   * Не начинается С
   */
  NOT_STARTS_WITH("notStartsWith"),
  /**
   * Этот диапазон пересекается с диапазоном
   */
  OVERLAPS_RANGE("overlapsRange"),
  /**
   * Этот массив пересекается с массивом
   */
  OVERLAPS_ARRAY("overlapsArray"),
  /**
   * Начинается С
   */
  STARTS_WITH("startsWith");

  private static final Map<String, FilterOperation> BY_CODE = new HashMap<>();

  static {
    for (FilterOperation type : FilterOperation.values()) {
      BY_CODE.put(type.getCode().toLowerCase(), type);
    }
  }

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
    return code == null ? null : BY_CODE.get(code.toLowerCase());
  }
}
