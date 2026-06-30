package mp.jprime.dataaccess.params.query.filters.value;

import mp.jprime.dataaccess.enums.FilterOperation;


/**
 * Не начинается С
 */
public class NotStartsWith extends CustomValueFilter<Object> {
  /**
   * Конструктор
   *
   * @param customValue Произвольное значение
   * @param value       Условие
   */
  public NotStartsWith(Object customValue, Object value) {
    super(customValue, value);
  }

  @Override
  public FilterOperation getOper() {
    return FilterOperation.NOT_STARTS_WITH;
  }
}

