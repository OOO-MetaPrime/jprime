package mp.jprime.dataaccess.params.query.filters.value;

import mp.jprime.dataaccess.enums.FilterOperation;


import java.util.Collection;

/**
 * Не в указанном списке
 */
public class NotIN extends CustomValueFilter<Collection<? extends Comparable>> {
  /**
   * Конструктор
   *
   * @param customValue Произвольное значение
   * @param value       Условие
   */
  public NotIN(Object customValue, Collection<? extends Comparable> value) {
    super(customValue, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.NOT_IN;
  }
}
