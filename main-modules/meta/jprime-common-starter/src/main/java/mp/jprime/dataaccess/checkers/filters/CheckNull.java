package mp.jprime.dataaccess.checkers.filters;

import mp.jprime.dataaccess.JPAttrData;
import mp.jprime.dataaccess.params.query.filters.Null;
import mp.jprime.dataaccess.params.query.filters.annotations.FilterLink;
import mp.jprime.security.AuthInfo;

/**
 * Null
 */
@FilterLink(
    exprClass = Null.class
)
public class CheckNull extends CheckBaseFilter<Null> {
  @Override
  public boolean check(Null filter, JPAttrData data, AuthInfo auth, boolean notContainsDefaultValue) {
    String attrCode = filter.getAttrCode();
    if (!data.containsAttr(attrCode)) {
      return notContainsDefaultValue;
    }
    Object attrValue = attrCode != null ? data.get(attrCode) : null;
    if (attrValue == null) {
      return Boolean.TRUE;
    }
    return Boolean.FALSE;
  }
}
