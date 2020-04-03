package mp.jprime.dataaccess.params.query.filters;

import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.dataaccess.params.query.enums.BooleanCondition;

import java.util.Collection;

/**
 * Объединение условий по И
 */
public class And extends BooleanFilter {
  /**
   * Конструктор
   *
   * @param filters Условия
   */
  public And(Filter... filters) {
    super(filters);
  }

  /**
   * Конструктор
   *
   * @param filters Условия
   */
  public And(Collection<Filter> filters) {
    super(filters);
  }

  /**
   * Условие
   *
   * @return Условие
   */
  @Override
  public BooleanCondition getCond() {
    return BooleanCondition.AND;
  }
}
