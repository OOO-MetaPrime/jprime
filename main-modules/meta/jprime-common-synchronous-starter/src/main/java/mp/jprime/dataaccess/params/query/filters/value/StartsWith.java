package mp.jprime.dataaccess.params.query.filters.value;

import mp.jprime.dataaccess.enums.FilterOperation;


/**
 * Начинается С
 */
public class StartsWith extends CustomValueFilter<Object> {
  /**
   * Конструктор
   *
   * @param customValue Произвольное значение
   * @param value       Условие
   */
  public StartsWith(Object customValue, Object value) {
    super(customValue, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.STARTS_WITH;
  }
}

