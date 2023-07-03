package mp.jprime.dataaccess.checkers.filters;

import mp.jprime.dataaccess.params.query.filters.NEQ;
import mp.jprime.dataaccess.params.query.filters.annotations.FilterLink;
import mp.jprime.lang.JPMap;
import mp.jprime.security.AuthInfo;

/**
 * Не равно
 */
@FilterLink(
    exprClass = NEQ.class
)
public class CheckNEQ extends CheckBaseFilter<NEQ> {
  @Override
  public boolean check(NEQ filter, JPMap data, AuthInfo auth, boolean notContainsDefaultValue) {
    Object filterValue = filter.getValue();

    String attrCode = filter.getAttrCode();
    if (!data.containsKey(attrCode)) {
      return notContainsDefaultValue;
    }
    Object attrValue  = attrCode != null ? data.get(attrCode) : null;
    if (filterValue == null && attrValue == null) {
      return Boolean.FALSE;
    } else if (filterValue != null && attrValue != null) {
      Object parseFilterFalue = parseTo(attrValue.getClass(), filterValue, auth);
      return parseFilterFalue != null && !parseFilterFalue.equals(attrValue);
    }
    return Boolean.TRUE;
  }
}
