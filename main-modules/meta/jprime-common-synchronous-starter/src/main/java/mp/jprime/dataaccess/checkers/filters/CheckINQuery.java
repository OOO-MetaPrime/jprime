package mp.jprime.dataaccess.checkers.filters;

import mp.jprime.dataaccess.JPSubQueryService;
import mp.jprime.dataaccess.JPSubQueryServiceAware;
import mp.jprime.dataaccess.params.JPSubQuery;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.dataaccess.params.query.filters.INQuery;
import mp.jprime.dataaccess.params.query.filters.annotations.FilterLink;
import mp.jprime.lang.JPMap;
import mp.jprime.security.AuthInfo;

import java.util.Collection;

/**
 * В указанном подзапросе
 */
@FilterLink(
    exprClass = INQuery.class
)
public class CheckINQuery extends CheckBaseFilter<INQuery> implements JPSubQueryServiceAware {
  private JPSubQueryService service;

  @Override
  public void setJpSubQueryService(JPSubQueryService service) {
    this.service = service;
  }

  @Override
  public boolean check(INQuery filter, JPMap data, AuthInfo auth, boolean notContainsDefaultValue) {
    String attrCode = filter.getAttrCode();
    if (!data.containsKey(attrCode)) {
      return notContainsDefaultValue;
    }
    JPSubQuery subQuery = filter.getValue();

    Collection<Comparable> values = subQuery != null ? service.getValues(subQuery, auth) : null;
    if (values == null || values.isEmpty()) {
      return false;
    }
    return getJpDataCheckService().check(Filter.attr(attrCode).in(values), data, auth, notContainsDefaultValue);
  }
}