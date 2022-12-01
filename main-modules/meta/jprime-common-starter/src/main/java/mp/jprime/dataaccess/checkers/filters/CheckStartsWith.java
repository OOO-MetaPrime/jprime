package mp.jprime.dataaccess.checkers.filters;

import mp.jprime.dataaccess.JPAttrData;
import mp.jprime.dataaccess.params.query.filters.StartsWith;
import mp.jprime.dataaccess.params.query.filters.annotations.FilterLink;
import mp.jprime.security.AuthInfo;

/**
 * Начинается С
 */
@FilterLink(
    exprClass = StartsWith.class
)
public class CheckStartsWith extends CheckBaseFilter<StartsWith> {
  @Override
  public boolean check(StartsWith filter, JPAttrData data, AuthInfo auth, boolean notContainsDefaultValue) {
    Object filterValue = filter.getValue();

    String attrCode = filter.getAttrCode();
    if (!data.containsAttr(attrCode)) {
      return notContainsDefaultValue;
    }
    Object attrValue = attrCode != null ? data.get(attrCode) : null;
    if (attrValue != null) {
      String parseAttrValue = parseTo(String.class, attrValue, auth);
      String parseFilterValue = parseTo(String.class, filterValue, auth);
      return parseAttrValue != null && parseFilterValue != null && parseAttrValue.startsWith(parseFilterValue);
    }
    return Boolean.FALSE;
  }
}
