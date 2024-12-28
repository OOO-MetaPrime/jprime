package mp.jprime.dataaccess.checkers;

import mp.jprime.dataaccess.Source;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.checkers.filters.CheckFilter;
import mp.jprime.common.JPOrderDirection;
import mp.jprime.dataaccess.params.JPSelect;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.common.JPOrder;
import mp.jprime.dataaccess.params.query.filters.annotations.FilterLink;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.lang.JPMap;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.exceptions.JPSelectRightException;
import mp.jprime.security.services.JPResourceAccess;
import mp.jprime.security.services.JPResourceAccessService;
import mp.jprime.security.services.JPResourceAccessServiceAware;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Сервис проверки данных указанному условию
 */
@Service
public final class JPDataCheckCommonService implements JPDataCheckService, JPResourceAccessServiceAware {
  private final Map<Class, CheckFilter> checkFilters = new HashMap<>();

  // Проверка доступа
  private JPResourceAccessService resourceAccessService;

  @Override
  public void setJpResourceAccessService(JPResourceAccessService accessService) {
    this.resourceAccessService = accessService;
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
   * Возвращает количество объектов, удовлетворяющих выборке
   *
   * @param select  JPSelect
   * @param objects Полный список объектов
   * @return Количество в выборке
   */
  @Override
  public Long getTotalCount(JPSelect select, Collection<JPObject> objects) {
    if (objects == null || objects.isEmpty()) {
      return 0L;
    }
    Stream<JPObject> stream = getListStream(select, objects);
    return stream.count();
  }

  /**
   * Фильтрует переданный список объектов по условию JPSelect
   *
   * @param select  JPSelect
   * @param objects Полный список объектов
   * @return Результирующий список
   */
  @Override
  public Collection<JPObject> getList(JPSelect select, Collection<JPObject> objects) {
    if (objects == null || objects.isEmpty()) {
      return Collections.emptyList();
    }
    Stream<JPObject> stream = getListStream(select, objects);
    stream = stream
        .sorted((o1, o2) -> {
          for (JPOrder order : select.getOrderBy()) {
            Comparable v1 = o1.getAttrValue(order.getAttr());
            Comparable v2 = o2.getAttrValue(order.getAttr());
            if (v1 instanceof String s1 && v2 instanceof String s2) {
              return (order.getOrder() == JPOrderDirection.ASC ? 1 : -1) * s1.compareToIgnoreCase(s2);
            }
            int compare = ObjectUtils.compare(v1, v2);
            if (compare != 0) {
              return order.getOrder() == JPOrderDirection.ASC ? compare : -1 * compare;
            }
          }
          return 0;
        });
    if (select.getOffset() != null) {
      stream = stream.skip(select.getOffset());
    }
    if (select.getLimit() != null) {
      stream = stream.limit(select.getLimit());
    }
    return stream.collect(Collectors.toList());
  }

  private Stream<JPObject> getListStream(JPSelect select, Collection<JPObject> objects) {
    String classCode = select.getJpClass();
    AuthInfo auth = select.getAuth();
    JPResourceAccess access = select.getSource() == Source.USER ? resourceAccessService.checkRead(classCode, auth) : null;
    if (access != null && !access.isAccess()) {
      throw new JPSelectRightException(classCode);
    }
    Stream<JPObject> stream = objects.stream();

    Filter filter = select.getWhere();
    if (filter != null) {
      stream = stream
          .filter(x -> check(filter, x.getData(), select.getAuth(), false));
    }
    return stream;
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
  public boolean check(Filter filter, JPMap data, AuthInfo auth, boolean notContainsDefaultValue) {
    if (filter == null) {
      return Boolean.TRUE;
    }
    CheckFilter checkFilter = checkFilters.get(filter.getClass());
    return checkFilter == null ? Boolean.FALSE : checkFilter.check(filter, data, auth, notContainsDefaultValue);
  }
}
