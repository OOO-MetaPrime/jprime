package mp.jprime.dataaccess.checkers.filters;

import mp.jprime.dataaccess.params.query.filters.NotIN;
import mp.jprime.dataaccess.params.query.filters.annotations.FilterLink;
import mp.jprime.lang.JPArray;
import mp.jprime.lang.JPMap;
import mp.jprime.security.AuthInfo;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * Не в указанном списке
 */
@FilterLink(
    exprClass = NotIN.class
)
public class CheckNotIN extends CheckBaseFilter<NotIN> {
  @Override
  public boolean check(NotIN filter, JPMap data, AuthInfo auth, boolean notContainsDefaultValue) {
    String attrCode = filter.getAttrCode();
    if (!data.containsKey(attrCode)) {
      return notContainsDefaultValue;
    }
    Object attrValue = attrCode != null ? data.get(attrCode) : null;
    if (attrValue == null) {
      return Boolean.TRUE;
    }
    Class valueClass = getValueClass(attrValue);
    if (valueClass == null) {
      return Boolean.FALSE;
    }

    ParsedCollection parsedCollection = parseToCollection(valueClass, filter.getValue(), auth);
    Collection filterValue = parsedCollection.values();
    if (filterValue == null || filterValue.isEmpty()) {
      return Boolean.TRUE;
    }

    if (attrValue instanceof Collection<?> aC) {
      return !CollectionUtils.containsAny(aC, filterValue);
    } else if (attrValue instanceof JPArray<?> aA) {
      return !CollectionUtils.containsAny(aA.toList(), filterValue);
    } else {
      return !filterValue.contains(attrValue);
    }
  }
}