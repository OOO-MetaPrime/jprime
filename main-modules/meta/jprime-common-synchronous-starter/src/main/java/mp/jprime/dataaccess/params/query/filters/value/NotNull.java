package mp.jprime.dataaccess.params.query.filters.value;

import mp.jprime.dataaccess.enums.FilterOperation;


/**
 * Значение указано
 */
public class NotNull extends CustomValueFilter<Comparable> {
  /**
   * Конструктор
   *
   * @param customValue Произвольное значение
   */
  public NotNull(Object customValue) {
    super(customValue, null);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.ISNOTNULL;
  }
}
