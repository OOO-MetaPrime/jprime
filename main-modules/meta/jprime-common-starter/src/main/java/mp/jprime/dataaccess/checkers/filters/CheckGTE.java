package mp.jprime.dataaccess.checkers.filters;

import mp.jprime.dataaccess.params.query.filters.GTE;
import mp.jprime.dataaccess.params.query.filters.annotations.FilterLink;
import mp.jprime.lang.JPMap;
import mp.jprime.security.AuthInfo;
import org.apache.commons.lang3.ObjectUtils;

/**
 * Больше или равно
 */
@FilterLink(
    exprClass = GTE.class
)
public class CheckGTE extends CheckBaseFilter<GTE> {
  @Override
  public boolean check(GTE filter, JPMap data, AuthInfo auth, boolean notContainsDefaultValue) {
    Object value = filter.getValue();

    String attrCode = filter.getAttrCode();
    if (!data.containsKey(attrCode)) {
      return notContainsDefaultValue;
    }
    Object attrValue = attrCode != null ? data.get(attrCode) : null;
    if (attrValue == null) {
      return Boolean.FALSE;
    }
    return ObjectUtils.compare(
        (Comparable) attrValue,
        (Comparable) parseTo(attrValue.getClass(), value, auth)
    ) >= 0;
  }
}