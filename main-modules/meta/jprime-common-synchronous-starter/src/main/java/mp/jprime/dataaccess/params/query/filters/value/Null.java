package mp.jprime.dataaccess.params.query.filters.value;

import mp.jprime.dataaccess.enums.FilterOperation;


/**
 * Значение не указано
 */
public class Null extends CustomValueFilter<Comparable> {
  /**
   * Конструктор
   *
   * @param customValue Произвольное значение
   */
  public Null(Object customValue) {
    super(customValue, null);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.ISNULL;
  }
}
