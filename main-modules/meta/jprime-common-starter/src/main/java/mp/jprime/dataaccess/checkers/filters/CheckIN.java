package mp.jprime.dataaccess.checkers.filters;

import mp.jprime.dataaccess.params.query.filters.IN;
import mp.jprime.dataaccess.params.query.filters.annotations.FilterLink;
import mp.jprime.lang.JPMap;
import mp.jprime.security.AuthInfo;
import org.checkerframework.checker.units.qual.C;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * В указанном списке
 */
@FilterLink(
    exprClass = IN.class
)
public class CheckIN extends CheckBaseFilter<IN> {
  @Override
  public boolean check(IN filter, JPMap data, AuthInfo auth, boolean notContainsDefaultValue) {
    Collection<? extends Comparable> filterValue = filter.getValue();

    String attrCode = filter.getAttrCode();
    if (!data.containsKey(attrCode)) {
      return notContainsDefaultValue;
    }
    Object attrValue = attrCode != null ? data.get(attrCode) : null;
    if (attrValue == null) {
      return Boolean.FALSE;
    }
    if (attrValue instanceof Collection<?> aC) {
      return CollectionUtils.containsAny(aC, filterValue);
    } else {
      Collection<? extends Object> p = parseToCollection(attrValue.getClass(), filterValue, auth);
      return p.contains(attrValue);
    }
  }
}