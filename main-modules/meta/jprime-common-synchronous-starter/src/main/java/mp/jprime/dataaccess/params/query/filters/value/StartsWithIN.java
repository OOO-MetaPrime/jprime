package mp.jprime.dataaccess.params.query.filters.value;

import mp.jprime.dataaccess.enums.FilterOperation;

import java.util.Collection;

/**
 * Значение начинается с указанного в списке
 */
public class StartsWithIN extends CustomValueFilter<Collection<String>> {
  /**
   * Конструктор
   *
   * @param customValue Произвольное значение
   * @param value    Условие
   */
  public StartsWithIN(Object customValue, Collection<String> value) {
    super(customValue, value);
  }

  @Override
  public FilterOperation getOper() {
    return FilterOperation.STARTS_WITH_IN;
  }
}
