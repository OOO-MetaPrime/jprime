package mp.jprime.dataaccess.services;

import mp.jprime.annotations.JPClassesLink;
import mp.jprime.common.JPClassesLinkFilter;
import mp.jprime.dataaccess.handlers.JPClassHandlerStorage;
import mp.jprime.dataaccess.handlers.JPClassHandler;
import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.dataaccess.params.JPDelete;
import mp.jprime.dataaccess.params.JPUpdate;
import mp.jprime.exceptions.JPRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Логика вызова CRUD-хендлеров
 */
@Service
public final class JPClassHandlerStorageService implements JPClassHandlerStorage, JPClassesLinkFilter<JPClassHandler> {
  private Map<String, Collection<JPClassHandler>> jpClassHandlers = new HashMap<>();
  private Collection<JPClassHandler> uniHandlers = new ArrayList<>();

  /**
   * Считываем аннотации
   */
  @Autowired(required = false)
  private void setHanlders(Collection<JPClassHandler> handlers) {
    if (handlers == null) {
      return;
    }
    Map<String, Collection<JPClassHandler>> jpClassHandlers = new HashMap<>();
    Collection<JPClassHandler> uniHandlers = new ArrayList<>();
    for (JPClassHandler handler : handlers) {
      try {
        JPClassesLink anno = handler.getClass().getAnnotation(JPClassesLink.class);
        if (anno == null) {
          continue;
        }
        for (String jpClassCode : anno.jpClasses()) {
          if (jpClassCode == null || jpClassCode.isEmpty()) {
            continue;
          }
          if ("*".equals(jpClassCode)) {
            uniHandlers.add(handler);
          } else {
            jpClassHandlers.computeIfAbsent(jpClassCode, x -> new ArrayList<>()).add(handler);
          }
        }
      } catch (Exception e) {
        throw JPRuntimeException.wrapException(e);
      }
    }
    uniHandlers = filter(uniHandlers);

    // Размечаем * аннотацию
    Collection<String> keys = jpClassHandlers.keySet();
    for (String key : keys) {
      Collection<JPClassHandler> values = jpClassHandlers.get(key);
      values.addAll(uniHandlers);
      jpClassHandlers.put(key, filter(values));
    }

    this.uniHandlers = filter(uniHandlers);
    this.jpClassHandlers = jpClassHandlers;
  }

  /**
   * Список хендлеров
   *
   * @param jpClassCode Кодовое имя класс
   * @return Список хендлеров
   */
  private Collection<JPClassHandler> getHandlers(String jpClassCode) {
    if (jpClassHandlers.containsKey(jpClassCode)) {
      return jpClassHandlers.get(jpClassCode);
    } else {
      Collection<JPClassHandler> result = new ArrayList<>(uniHandlers);
      jpClassHandlers.put(jpClassCode, result);
      return result;
    }
  }

  /**
   * Перед созданием
   *
   * @param query JPCreate
   */
  @Override
  public void beforeCreate(JPCreate query) {
    Collection<JPClassHandler> handlers = getHandlers(query.getJpClass());
    if (handlers != null) {
      handlers.forEach(x -> x.beforeCreate(query));
    }
  }

  /**
   * Перед обновлением
   *
   * @param query JPUpdate
   */
  public void beforeUpdate(JPUpdate query) {
    Collection<JPClassHandler> handlers = getHandlers(query.getJpId().getJpClass());
    if (handlers != null) {
      handlers.forEach(x -> x.beforeUpdate(query));
    }
  }

  /**
   * После создания
   *
   * @param objectId Идентификатор объекта
   * @param query    JPCreate
   */
  @Override
  public void afterCreate(Comparable objectId, JPCreate query) {
    Collection<JPClassHandler> handlers = getHandlers(query.getJpClass());
    if (handlers != null) {
      handlers.forEach(x -> x.afterCreate(objectId, query));
    }
  }

  /**
   * После обновления
   *
   * @param query JPUpdate
   */
  public void afterUpdate(JPUpdate query) {
    Collection<JPClassHandler> handlers = getHandlers(query.getJpId().getJpClass());
    if (handlers != null) {
      handlers.forEach(x -> x.afterUpdate(query));
    }
  }

  /**
   * Перед удалением
   *
   * @param query JPDelete
   */
  @Override
  public void beforeDelete(JPDelete query) {
    Collection<JPClassHandler> handlers = getHandlers(query.getJpClass());
    if (handlers != null) {
      handlers.forEach(x -> x.beforeDelete(query));
    }
  }

  /**
   * После удаления
   *
   * @param query JPDelete
   */
  public void afterDelete(JPDelete query) {
    Collection<JPClassHandler> handlers = getHandlers(query.getJpId().getJpClass());
    if (handlers != null) {
      handlers.forEach(x -> x.afterDelete(query));
    }
  }
}
