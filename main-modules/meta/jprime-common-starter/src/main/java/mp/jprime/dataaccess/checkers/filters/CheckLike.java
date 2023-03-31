package mp.jprime.dataaccess.checkers.filters;

import mp.jprime.dataaccess.JPAttrData;
import mp.jprime.dataaccess.params.query.filters.Like;
import mp.jprime.dataaccess.params.query.filters.annotations.FilterLink;
import mp.jprime.security.AuthInfo;
import org.apache.commons.lang3.StringUtils;

/**
 * Содержит
 */
@FilterLink(
    exprClass = Like.class
)
public class CheckLike extends CheckBaseFilter<Like> {
  @Override
  public boolean check(Like filter, JPAttrData data, AuthInfo auth, boolean notContainsDefaultValue) {
    Object filterValue = filter.getValue();

    String attrCode = filter.getAttrCode();
    if (!data.containsAttr(attrCode)) {
      return notContainsDefaultValue;
    }
    Object attrValue = attrCode != null ? data.get(attrCode) : null;
    if (attrValue != null) {
      String parseAttrValue = parseTo(String.class, attrValue, auth);
      String parseFilterValue = parseTo(String.class, filterValue, auth);
      return parseAttrValue != null && StringUtils.containsIgnoreCase(parseAttrValue, parseFilterValue);
    }
    return Boolean.FALSE;
  }
}
