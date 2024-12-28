package mp.jprime.dataaccess.checkers.filters;

import mp.jprime.dataaccess.JPSubQueryService;
import mp.jprime.dataaccess.JPSubQueryServiceAware;
import mp.jprime.dataaccess.params.JPSubQuery;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.dataaccess.params.query.filters.NotINQuery;
import mp.jprime.dataaccess.params.query.filters.annotations.FilterLink;
import mp.jprime.lang.JPMap;
import mp.jprime.security.AuthInfo;

import java.util.Collection;

/**
 * Не в указанном подзапросе
 */
@FilterLink(
    exprClass = NotINQuery.class
)
public class CheckNotINQuery extends CheckBaseFilter<NotINQuery> implements JPSubQueryServiceAware {
  private JPSubQueryService service;

  @Override
  public void setJpSubQueryService(JPSubQueryService service) {
    this.service = service;
  }

  @Override
  public boolean check(NotINQuery filter, JPMap data, AuthInfo auth, boolean notContainsDefaultValue) {
    String attrCode = filter.getAttrCode();
    if (!data.containsKey(attrCode)) {
      return notContainsDefaultValue;
    }
    Object attrValue = attrCode != null ? data.get(attrCode) : null;
    if (attrValue == null) {
      return Boolean.TRUE;
    }
    JPSubQuery subQuery = filter.getValue();

    Collection<Comparable> values = subQuery != null ? service.getValues(subQuery, auth) : null;
    if (values == null || values.isEmpty()) {
      return Boolean.TRUE;
    }
    return getJpDataCheckService().check(Filter.attr(attrCode).notIn(values), data, auth, notContainsDefaultValue);
  }
}