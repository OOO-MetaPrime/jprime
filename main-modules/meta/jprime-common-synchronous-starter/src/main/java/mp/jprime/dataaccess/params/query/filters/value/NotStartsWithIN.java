package mp.jprime.dataaccess.params.query.filters.value;

import mp.jprime.dataaccess.enums.FilterOperation;

import java.util.Collection;

/**
 * Значение не начинается с указанного в списке
 */
public class NotStartsWithIN extends CustomValueFilter<Collection<String>> {
  /**
   * Конструктор
   *
   * @param customValue Произвольное значение
   * @param value       Условие
   */
  public NotStartsWithIN(Object customValue, Collection<String> value) {
    super(customValue, value);
  }

  @Override
  public FilterOperation getOper() {
    return FilterOperation.NOT_STARTS_WITH_IN;
  }
}
