package mp.jprime.dataaccess.checkers.filters;

import mp.jprime.dataaccess.JPAttrData;
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
  public boolean check(IN filter, JPAttrData data, AuthInfo auth, boolean notContainsDefaultValue) {
    Collection<? extends Comparable> filterValue = filter.getValue();

    String attrCode = filter.getAttrCode();
    if (!data.containsAttr(attrCode)) {
      return notContainsDefaultValue;
    }
    Object attrValue = attrCode != null ? data.get(attrCode) : null;
    if (attrValue == null) {
      return Boolean.FALSE;
    }
    Collection<? extends Object> p = parseToCollection(attrValue.getClass(), filterValue, auth);
    return p.contains(attrValue);
  }
}