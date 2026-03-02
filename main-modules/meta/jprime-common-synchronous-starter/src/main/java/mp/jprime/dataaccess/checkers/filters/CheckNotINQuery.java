package mp.jprime.dataaccess.checkers.filters;

import mp.jprime.dataaccess.JPSubQueryService;
import mp.jprime.dataaccess.params.JPSubQuery;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.dataaccess.params.query.filters.attr.NotINQuery;
import mp.jprime.dataaccess.params.query.filters.attr.annotations.FilterLink;
import mp.jprime.lang.JPMap;
import mp.jprime.security.AuthInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/**
 * Не в указанном подзапросе
 */
@FilterLink(
    filterClass = NotINQuery.class
)
public class CheckNotINQuery extends CheckBaseFilter<NotINQuery> {
  private final JPSubQueryService service;

  private CheckNotINQuery(@Autowired JPSubQueryService service) {
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