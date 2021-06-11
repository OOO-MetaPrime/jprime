package mp.jprime.dataaccess.checkers.filters;

import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.dataaccess.params.query.filters.Or;
import mp.jprime.dataaccess.params.query.filters.annotations.FilterLink;
import mp.jprime.security.AuthInfo;

import java.util.Collection;

/**
 * Объединение условий по ИЛИ
 */
@FilterLink(
    exprClass = Or.class
)
public class CheckOr extends CheckBaseFilter<Or> {
  @Override
  public boolean check(Or filter, JPMutableData data, AuthInfo auth) {
    Collection<Filter> childs = filter.getFilters();
    if (childs == null || childs.isEmpty()) {
      return Boolean.TRUE;
    }
    boolean result = Boolean.FALSE;
    for (Filter child : childs) {
      result = result || getJpDataCheckService().check(child, data, auth);
    }
    return result;
  }
}
