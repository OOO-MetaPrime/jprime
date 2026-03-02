package mp.jprime.dataaccess.params.query.filters.value;

import mp.jprime.dataaccess.enums.FilterOperation;


/**
 * Нечеткий поиск(без парсинга строки)
 */
public class FuzzyQuery extends CustomValueFilter<String> {
  /**
   * Конструктор
   *
   * @param customValue Произвольное значение
   * @param value       Условие
   */
  public FuzzyQuery(Object customValue, String value) {
    super(customValue, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.FUZZY_QUERY;
  }
}
