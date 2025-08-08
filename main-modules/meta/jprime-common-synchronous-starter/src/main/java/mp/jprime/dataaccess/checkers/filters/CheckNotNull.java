package mp.jprime.dataaccess.checkers.filters;

import mp.jprime.dataaccess.params.query.filters.NotNull;
import mp.jprime.dataaccess.params.query.filters.annotations.FilterLink;
import mp.jprime.lang.JPArray;
import mp.jprime.lang.JPMap;
import mp.jprime.security.AuthInfo;

import java.util.Collection;

/**
 * не Null
 */
@FilterLink(
    exprClass = NotNull.class
)
public class CheckNotNull extends CheckBaseFilter<NotNull> {
  @Override
  public boolean check(NotNull filter, JPMap data, AuthInfo auth, boolean notContainsDefaultValue) {
    String attrCode = filter.getAttrCode();
    if (!data.containsKey(attrCode)) {
      return notContainsDefaultValue;
    }
    Object attrValue = attrCode != null ? data.get(attrCode) : null;
    if (attrValue instanceof Collection<?> aC) {
      return !aC.isEmpty();
    } else if (attrValue instanceof JPArray<?> aA) {
      return !aA.isEmpty();
    } else if (attrValue != null) {
      return Boolean.TRUE;
    }
    return Boolean.FALSE;
  }
}
