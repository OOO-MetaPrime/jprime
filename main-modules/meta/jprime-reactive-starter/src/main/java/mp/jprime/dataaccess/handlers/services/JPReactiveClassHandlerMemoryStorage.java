package mp.jprime.dataaccess.handlers.services;

import mp.jprime.annotations.JPClassesLink;
import mp.jprime.common.JPClassesLinkFilter;
import mp.jprime.dataaccess.defvalues.JPObjectDefValueService;
import mp.jprime.dataaccess.handlers.JPReactiveClassHandler;
import mp.jprime.dataaccess.handlers.JPReactiveClassHandlerStorage;
import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.dataaccess.params.JPDelete;
import mp.jprime.dataaccess.params.JPUpdate;
import mp.jprime.exceptions.JPRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Логика вызова CRUD-хендлеров
 */
@Service
public final class JPReactiveClassHandlerMemoryStorage implements JPReactiveClassHandlerStorage, JPClassesLinkFilter<JPReactiveClassHandler> {
  private Map<String, Collection<JPReactiveClassHandler>> jpClassHandlers = new HashMap<>();
  private Collection<JPReactiveClassHandler> uniHandlers = new ArrayList<>();
  /**
   * Логика вычисления значений по-умолчанию
   */
  private JPObjectDefValueService jpObjectDefValueService;

  @Autowired
  private void setJPObjectDefValueService(JPObjectDefValueService jpObjectDefValueService) {
    this.jpObjectDefValueService = jpObjectDefValueService;
  }

  /**
   * Считываем аннотации
   */
  @Autowired(required = false)
  private void setHandlers(Collection<JPReactiveClassHandler> handlers) {
    if (handlers == null) {
      return;
    }
    Map<String, Collection<JPReactiveClassHandler>> jpClassHandlers = new HashMap<>();
    Collection<JPReactiveClassHandler> uniHandlers = new ArrayList<>();
    for (JPReactiveClassHandler handler : handlers) {
      try {
        JPClassesLink anno = handler.getClass().getAnnotation(JPClassesLink.class);
        if (anno == null) {
          continue;
        }
        if (anno.uni()) {
          uniHandlers.add(handler);
        } else {
          for (String jpClassCode : anno.jpClasses()) {
            if (jpClassCode == null || jpClassCode.isEmpty()) {
              continue;
            }
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
      Collection<JPReactiveClassHandler> values = jpClassHandlers.get(key);
      values.addAll(uniHandlers);
      jpClassHandlers.put(key, filter(values));
    }

    this.uniHandlers = uniHandlers;
    this.jpClassHandlers = jpClassHandlers;
  }

  /**
   * Список хендлеров
   *
   * @param jpClassCode Кодовое имя класс
   * @return Список хендлеров
   */
  private Collection<JPReactiveClassHandler> getHandlers(String jpClassCode) {
    if (jpClassHandlers.containsKey(jpClassCode)) {
      return jpClassHandlers.get(jpClassCode);
    } else if (!uniHandlers.isEmpty()) {
      Collection<JPReactiveClassHandler> result = new ArrayList<>(uniHandlers);
      jpClassHandlers.put(jpClassCode, result);
      return result;
    } else {
      return null;
    }
  }

  /**
   * Перед созданием
   *
   * @param query JPCreate
   */
  @Override
  public Mono<Void> beforeCreate(JPCreate query) {
    query.getData().putIfAbsent(
        jpObjectDefValueService.getDefValues(query.getJpClass(), query.getAuth())
    );
    Collection<JPReactiveClassHandler> handlers = getHandlers(query.getJpClass());
    return handlers == null || handlers.isEmpty() ? Mono.empty() : Mono.when(
        handlers
            .stream()
            .map(x -> x.beforeCreate(query))
            .collect(Collectors.toList())
    );
  }

  /**
   * Перед обновлением
   *
   * @param query JPUpdate
   */
  public Mono<Void> beforeUpdate(JPUpdate query) {
    Collection<JPReactiveClassHandler> handlers = getHandlers(query.getJpId().getJpClass());
    return handlers == null || handlers.isEmpty() ? Mono.empty() : Mono.when(
        handlers
            .stream()
            .map(x -> x.beforeUpdate(query))
            .collect(Collectors.toList())
    );
  }

  /**
   * После создания
   *
   * @param objectId Идентификатор объекта
   * @param query    JPCreate
   */
  @Override
  public Mono<Void> afterCreate(Comparable objectId, JPCreate query) {
    Collection<JPReactiveClassHandler> handlers = getHandlers(query.getJpClass());
    return handlers == null || handlers.isEmpty() ? Mono.empty() : Mono.when(
        handlers
            .stream()
            .map(x -> x.afterCreate(objectId, query))
            .collect(Collectors.toList())
    );
  }

  /**
   * После обновления
   *
   * @param query JPUpdate
   */
  public Mono<Void> afterUpdate(JPUpdate query) {
    Collection<JPReactiveClassHandler> handlers = getHandlers(query.getJpId().getJpClass());
    return handlers == null || handlers.isEmpty() ? Mono.empty() : Mono.when(
        handlers
            .stream()
            .map(x -> x.afterUpdate(query))
            .collect(Collectors.toList())
    );
  }

  /**
   * Перед удалением
   *
   * @param query JPDelete
   */
  @Override
  public Mono<Void> beforeDelete(JPDelete query) {
    Collection<JPReactiveClassHandler> handlers = getHandlers(query.getJpClass());
    return handlers == null || handlers.isEmpty() ? Mono.empty() : Mono.when(
        handlers
            .stream()
            .map(x -> x.beforeDelete(query))
            .collect(Collectors.toList())
    );
  }

  /**
   * После удаления
   *
   * @param query JPDelete
   */
  public Mono<Void> afterDelete(JPDelete query) {
    Collection<JPReactiveClassHandler> handlers = getHandlers(query.getJpId().getJpClass());
    return handlers == null || handlers.isEmpty() ? Mono.empty() : Mono.when(
        handlers
            .stream()
            .map(x -> x.afterDelete(query))
            .collect(Collectors.toList())
    );
  }
}
