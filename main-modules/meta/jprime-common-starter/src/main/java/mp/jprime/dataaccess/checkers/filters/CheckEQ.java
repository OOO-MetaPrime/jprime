package mp.jprime.dataaccess.checkers.filters;

import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.dataaccess.params.query.filters.EQ;
import mp.jprime.dataaccess.params.query.filters.annotations.FilterLink;
import mp.jprime.security.AuthInfo;

/**
 * Равно
 */
@FilterLink(
    exprClass = EQ.class
)
public class CheckEQ extends CheckBaseFilter<EQ> {
  @Override
  public boolean check(EQ filter, JPMutableData data, AuthInfo auth) {
    Object filterValue = filter.getValue();

    String attrCode = filter.getAttrCode();
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
