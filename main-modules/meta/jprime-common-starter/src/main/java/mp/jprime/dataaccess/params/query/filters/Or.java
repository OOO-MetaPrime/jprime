package mp.jprime.dataaccess.params.query.filters;

import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.dataaccess.enums.BooleanCondition;

import java.util.Collection;

/**
 * Объединение условий по ИЛИ
 */
public class Or extends BooleanFilter {
  /**
   * Конструктор
   *
   * @param filters Условия
   */
  public Or(Filter... filters) {
    super(filters);
  }

  /**
   * Конструктор
   *
   * @param filters Условия
   */
  public Or(Collection<Filter> filters) {
    super(filters);
  }

  /**
   * Условие
   *
   * @return Условие
   */
  @Override
  public BooleanCondition getCond() {
    return BooleanCondition.OR;
  }
}
