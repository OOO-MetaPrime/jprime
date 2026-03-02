package mp.jprime.dataaccess.checkers.filters;

import mp.jprime.dataaccess.params.query.filters.attr.EQ;
import mp.jprime.dataaccess.params.query.filters.attr.annotations.FilterLink;
import mp.jprime.lang.JPMap;
import mp.jprime.security.AuthInfo;

/**
 * Равно
 */
@FilterLink(
    filterClass = EQ.class
)
public class CheckEQ extends CheckBaseFilter<EQ> {
  @Override
  public boolean check(EQ filter, JPMap data, AuthInfo auth, boolean notContainsDefaultValue) {
    Object filterValue = filter.getValue();

    String attrCode = filter.getAttrCode();
    if (!data.containsKey(attrCode)) {
      return notContainsDefaultValue;
    }
    Object attrValue  = attrCode != null ? data.get(attrCode) : null;
    if (filterValue == null && attrValue == null) {
      return Boolean.TRUE;
    } else if (attrValue != null) {
      Object parseFilterValue = parseTo(attrValue.getClass(), filterValue, auth);
      return parseFilterValue != null && parseFilterValue.equals(attrValue);
    }
    return Boolean.FALSE;
  }
}
