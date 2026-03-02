package mp.jprime.dataaccess.params.query.filters.value;

import mp.jprime.dataaccess.enums.FilterOperation;


import java.util.Collection;

/**
 * В указанном списке
 */
public class IN extends CustomValueFilter<Collection<? extends Comparable>> {
  /**
   * Конструктор
   *
   * @param customValue Произвольное значение
   * @param value       Условие
   */
  public IN(Object customValue, Collection<? extends Comparable> value) {
    super(customValue, value);
  }

  /**
   * Операция
   *
   * @return Операция
   */
  @Override
  public FilterOperation getOper() {
    return FilterOperation.IN;
  }
}
