package mp.jprime.dataaccess.params.query.filters;

import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.dataaccess.enums.BooleanCondition;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Логические условия
 */
public abstract class BooleanFilter extends Filter {
  private Collection<Filter> filters;

  /**
   * Конструктор
   *
   * @param filters Условия
   */
  protected BooleanFilter(Filter... filters) {
    this(filters != null ? Arrays.asList(filters) : null);
  }

  /**
   * Конструктор
   *
   * @param filters Условия
   */
  protected BooleanFilter(Collection<Filter> filters) {
    this.filters = Collections.unmodifiableCollection(filters != null ? filters : Collections.emptyList());
  }

  /**
   * Возвращает вложенные условия
   *
   * @return Вложенные условия
   */
  public Collection<Filter> getFilters() {
    return filters;
  }

  /**
   * Условие
   *
   * @return Условие
   */
  abstract public BooleanCondition getCond();
}
