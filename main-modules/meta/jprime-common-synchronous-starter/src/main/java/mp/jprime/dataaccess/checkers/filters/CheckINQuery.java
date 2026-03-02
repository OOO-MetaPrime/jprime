package mp.jprime.dataaccess.checkers.filters;

import mp.jprime.dataaccess.JPSubQueryService;
import mp.jprime.dataaccess.params.JPSubQuery;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.dataaccess.params.query.filters.attr.INQuery;
import mp.jprime.dataaccess.params.query.filters.attr.annotations.FilterLink;
import mp.jprime.lang.JPMap;
import mp.jprime.security.AuthInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/**
 * В указанном подзапросе
 */
@FilterLink(
    filterClass = INQuery.class
)
public class CheckINQuery extends CheckBaseFilter<INQuery> {
  private final JPSubQueryService service;

  private CheckINQuery(@Autowired JPSubQueryService service) {
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