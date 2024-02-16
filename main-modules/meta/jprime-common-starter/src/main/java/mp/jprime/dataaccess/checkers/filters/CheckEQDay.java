package mp.jprime.dataaccess.checkers.filters;

import mp.jprime.dataaccess.params.query.filters.EQDay;
import mp.jprime.dataaccess.params.query.filters.annotations.FilterLink;
import mp.jprime.lang.JPMap;
import mp.jprime.security.AuthInfo;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * День равен (Дата)
 */
@FilterLink(
    exprClass = EQDay.class
)
public class CheckEQDay extends CheckBaseFilter<EQDay> {

  @Override
  public boolean check(EQDay filter, JPMap data, AuthInfo auth, boolean notContainsDefaultValue) {
    LocalDate filterValue = filter.getValue();

    String attrCode = filter.getAttrCode();
    if (!data.containsKey(attrCode)) {
      return notContainsDefaultValue;
    }
    Object attrValue = attrCode != null ? data.get(attrCode) : null;
    if (filterValue == null && attrValue == null) {
      return Boolean.TRUE;
    } else if (filterValue != null) {
      LocalDate parseAttrValue = parseTo(filterValue.getClass(), attrValue, auth);
      return parseAttrValue != null && parseAttrValue.equals(filterValue);
    }
    return Boolean.FALSE;
  }

}
