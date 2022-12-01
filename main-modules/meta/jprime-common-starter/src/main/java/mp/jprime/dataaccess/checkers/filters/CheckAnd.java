package mp.jprime.dataaccess.checkers.filters;

import mp.jprime.dataaccess.JPAttrData;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.dataaccess.params.query.filters.And;
import mp.jprime.dataaccess.params.query.filters.annotations.FilterLink;
import mp.jprime.security.AuthInfo;

import java.util.Collection;

/**
 * Объединение условий по И
 */
@FilterLink(
    exprClass = And.class
)
public class CheckAnd extends CheckBaseFilter<And> {
  @Override
  public boolean check(And filter, JPAttrData data, AuthInfo auth, boolean notContainsDefaultValue) {
    Collection<Filter> childs = filter.getFilters();
    if (childs == null || childs.isEmpty()) {
      return Boolean.TRUE;
    }
    boolean result = Boolean.TRUE;
    for (Filter child : childs) {
      result = result && getJpDataCheckService().check(child, data, auth, notContainsDefaultValue);
    }
    return result;
  }
}
