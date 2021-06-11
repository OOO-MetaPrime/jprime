package mp.jprime.dataaccess.checkers.filters;

import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.dataaccess.params.query.filters.IN;
import mp.jprime.dataaccess.params.query.filters.annotations.FilterLink;
import mp.jprime.security.AuthInfo;

import java.util.Collection;

/**
 * В указанном списке
 */
@FilterLink(
    exprClass = IN.class
)
public class CheckIN extends CheckBaseFilter<IN> {
  @Override
  public boolean check(IN filter, JPMutableData data, AuthInfo auth) {
    Collection<? extends Comparable> filterValue = filter.getValue();

    String attrCode = filter.getAttrCode();
    Object attrValue = attrCode != null ? data.get(attrCode) : null;
    if (attrValue == null) {
      return Boolean.FALSE;
    }
    Collection<Object> p = parseTo(attrValue.getClass(), filterValue, auth);
    return p.contains(attrValue);
  }
}