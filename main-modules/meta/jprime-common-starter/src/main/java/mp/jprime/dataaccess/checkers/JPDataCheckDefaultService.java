package mp.jprime.dataaccess.checkers;

import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.dataaccess.checkers.filters.CheckFilter;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.dataaccess.params.query.filters.annotations.FilterLink;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.security.AuthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Сервис проверки данных указанному условию
 */
@Service
public final class JPDataCheckDefaultService implements JPDataCheckService {
  private final Map<Class, CheckFilter> checkFilters = new HashMap<>();

  @Autowired(required = false)
  private void setFilters(Collection<CheckFilter> filters) {
    if (filters == null) {
      return;
    }
    for (CheckFilter filter : filters) {
      try {
        FilterLink anno = filter.getClass().getAnnotation(FilterLink.class);
        if (anno == null) {
          continue;
        }
        checkFilters.put(anno.exprClass(), filter);
      } catch (Exception e) {
        throw JPRuntimeException.wrapException(e);
      }
    }
  }

  @Override
  public boolean check(Filter filter, JPMutableData data, AuthInfo auth) {
    if (filter == null) {
      return Boolean.TRUE;
    }
    CheckFilter checkFilter = checkFilters.get(filter.getClass());
    return checkFilter == null ? Boolean.FALSE : checkFilter.check(filter, data, auth);
  }
}
