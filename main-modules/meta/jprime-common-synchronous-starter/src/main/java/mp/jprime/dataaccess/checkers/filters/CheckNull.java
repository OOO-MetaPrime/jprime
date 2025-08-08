package mp.jprime.dataaccess.checkers.filters;

import mp.jprime.dataaccess.params.query.filters.Null;
import mp.jprime.dataaccess.params.query.filters.annotations.FilterLink;
import mp.jprime.lang.JPArray;
import mp.jprime.lang.JPMap;
import mp.jprime.security.AuthInfo;

import java.util.Collection;

/**
 * Null
 */
@FilterLink(
    exprClass = Null.class
)
public class CheckNull extends CheckBaseFilter<Null> {
  @Override
  public boolean check(Null filter, JPMap data, AuthInfo auth, boolean notContainsDefaultValue) {
    String attrCode = filter.getAttrCode();
    if (!data.containsKey(attrCode)) {
      return notContainsDefaultValue;
    }
    Object attrValue = attrCode != null ? data.get(attrCode) : null;
    if (attrValue instanceof Collection<?> aC) {
      return aC.isEmpty();
    } else if (attrValue instanceof JPArray<?> aA) {
      return aA.isEmpty();
    } else if (attrValue == null) {
      return Boolean.TRUE;
    }
    return Boolean.FALSE;
  }
}
