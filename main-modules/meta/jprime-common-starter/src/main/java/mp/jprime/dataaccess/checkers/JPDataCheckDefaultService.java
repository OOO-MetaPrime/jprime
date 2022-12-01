package mp.jprime.dataaccess.checkers;

import mp.jprime.dataaccess.JPAttrData;
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

  /**
   * Указание ссылок
   */
  @Autowired(required = false)
  private void setAwares(Collection<JPDataCheckServiceAware> awares) {
    for (JPDataCheckServiceAware aware : awares) {
      aware.setJpDataCheckService(this);
    }
  }

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

  /**
   * Проверяем условие по переданным данным
   *
   * @param filter                  Условие
   * @param data                    Данные
   * @param auth                    AuthInfo
   * @param notContainsDefaultValue Результат, в случае отсутствия ключа в data
   * @return Да/Нет
   */
  @Override
  public boolean check(Filter filter, JPAttrData data, AuthInfo auth, boolean notContainsDefaultValue) {
    if (filter == null) {
      return Boolean.TRUE;
    }
    CheckFilter checkFilter = checkFilters.get(filter.getClass());
    return checkFilter == null ? Boolean.FALSE : checkFilter.check(filter, data, auth, notContainsDefaultValue);
  }
}
