package mp.jprime.dataaccess.checkers.filters;

import mp.jprime.dataaccess.params.query.filters.IN;
import mp.jprime.dataaccess.params.query.filters.annotations.FilterLink;
import mp.jprime.lang.JPArray;
import mp.jprime.lang.JPMap;
import mp.jprime.security.AuthInfo;
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
    String attrCode = filter.getAttrCode();
    if (!data.containsKey(attrCode)) {
      return notContainsDefaultValue;
    }
    Object attrValue = attrCode != null ? data.get(attrCode) : null;
    if (attrValue == null) {
      return Boolean.FALSE;
    }
    Class valueClass = getValueClass(attrValue);
    if (valueClass == null) {
      return Boolean.FALSE;
    }

    ParsedCollection parsedCollection = parseToCollection(valueClass, filter.getValue(), auth);
    Collection filterValue = parsedCollection.values();
    if (filterValue == null || filterValue.isEmpty()) {
      return parsedCollection.emptyValuesIgnore();
    }
    if (attrValue instanceof Collection<?> aC) {
      return CollectionUtils.containsAny(aC, filterValue);
    } else if (attrValue instanceof JPArray<?> aA) {
      return CollectionUtils.containsAny(aA.toList(), filterValue);
    } else {
      return filterValue.contains(attrValue);
    }
  }
}