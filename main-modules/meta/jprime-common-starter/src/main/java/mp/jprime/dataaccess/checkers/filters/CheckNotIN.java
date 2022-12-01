package mp.jprime.dataaccess.checkers.filters;

import mp.jprime.dataaccess.JPAttrData;
import mp.jprime.dataaccess.params.query.filters.NotIN;
import mp.jprime.dataaccess.params.query.filters.annotations.FilterLink;
import mp.jprime.security.AuthInfo;

import java.util.Collection;

/**
 * В указанном списке
 */
@FilterLink(
    exprClass = NotIN.class
)
public class CheckNotIN extends CheckBaseFilter<NotIN> {
  @Override
  public boolean check(NotIN filter, JPAttrData data, AuthInfo auth, boolean notContainsDefaultValue) {
    Collection<? extends Comparable> filterValue = filter.getValue();

    String attrCode = filter.getAttrCode();
    if (!data.containsAttr(attrCode)) {
      return notContainsDefaultValue;
    }
    Object attrValue = attrCode != null ? data.get(attrCode) : null;
    if (attrValue == null) {
      return Boolean.TRUE;
    }
    Collection<? extends Object> p = parseToCollection(attrValue.getClass(), filterValue, auth);
    return !p.contains(attrValue);
  }
}