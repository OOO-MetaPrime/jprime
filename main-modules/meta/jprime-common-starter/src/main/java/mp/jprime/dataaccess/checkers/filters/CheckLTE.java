package mp.jprime.dataaccess.checkers.filters;

import mp.jprime.dataaccess.JPAttrData;
import mp.jprime.dataaccess.params.query.filters.LTE;
import mp.jprime.dataaccess.params.query.filters.annotations.FilterLink;
import mp.jprime.security.AuthInfo;
import org.apache.commons.lang3.ObjectUtils;


/**
 * Меньше или равно
 */
@FilterLink(
    exprClass = LTE.class
)
public class CheckLTE extends CheckBaseFilter<LTE> {
  @Override
  public boolean check(LTE filter, JPAttrData data, AuthInfo auth, boolean notContainsDefaultValue) {
    Object value = filter.getValue();

    String attrCode = filter.getAttrCode();
    if (!data.containsAttr(attrCode)) {
      return notContainsDefaultValue;
    }
    Object attrValue = attrCode != null ? data.get(attrCode) : null;
    if (attrValue == null) {
      return Boolean.FALSE;
    }
    return ObjectUtils.compare(
        (Comparable) attrValue,
        (Comparable) parseTo(attrValue.getClass(), value, auth)
    ) <= 0;
  }
}
