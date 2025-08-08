package mp.jprime.dataaccess.handlers.services;

import mp.jprime.annotations.JPClassesLink;
import mp.jprime.common.JPClassesLinkFilter;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.handlers.JPClassHandler;
import mp.jprime.dataaccess.handlers.JPClassHandlerStorage;
import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.dataaccess.params.JPDelete;
import mp.jprime.dataaccess.params.JPUpdate;
import mp.jprime.exceptions.JPRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Логика вызова CRUD-хендлеров
 */
@Service
public final class JPClassHandlerMemoryStorage implements JPClassHandlerStorage, JPClassesLinkFilter<JPClassHandler> {
  private Map<String, Collection<JPClassHandler>> jpClassHandlers = new HashMap<>();
  private Collection<JPClassHandler> uniHandlers = new ArrayList<>();

  /**
   * Считываем аннотации
   */
  @Autowired(required = false)
  private void setHandlers(Collection<JPClassHandler> handlers) {
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
      Collection<JPClassHandler> values = jpClassHandlers.get(key);
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
  private Collection<JPClassHandler> getHandlers(String jpClassCode) {
    if (jpClassHandlers.containsKey(jpClassCode)) {
      return jpClassHandlers.get(jpClassCode);
    } else if (!uniHandlers.isEmpty()) {
      Collection<JPClassHandler> result = new ArrayList<>(uniHandlers);
      jpClassHandlers.put(jpClassCode, result);
      return result;
    } else {
      return null;
    }
  }

  @Override
  public JPId find(JPCreate query) {
    Collection<JPClassHandler> handlers = getHandlers(query.getJpClass());
    if (handlers != null) {
      for (JPClassHandler handler : handlers) {
        JPId id = handler.find(query);
        if (id != null) {
          return id;
        }
      }
    }
    return null;
  }

  @Override
  public void beforeCommitCreate(JPCreate query) {
    Collection<JPClassHandler> handlers = getHandlers(query.getJpClass());
    if (handlers != null) {
      handlers.forEach(x -> x.beforeCommitCreate(query));
    }
  }

  @Override
  public void beforeCommitUpdate(JPUpdate query) {
    Collection<JPClassHandler> handlers = getHandlers(query.getJpId().getJpClass());
    if (handlers != null) {
      handlers.forEach(x -> x.beforeCommitUpdate(query));
    }
  }

  @Override
  public void beforeCommitDelete(JPDelete query) {
    Collection<JPClassHandler> handlers = getHandlers(query.getJpClass());
    if (handlers != null) {
      handlers.forEach(x -> x.beforeCommitDelete(query));
    }
  }

  @Override
  public void beforeCreate(JPCreate query) {
    Collection<JPClassHandler> handlers = getHandlers(query.getJpClass());
    if (handlers != null) {
      handlers.forEach(x -> x.beforeCreate(query));
    }
  }

  @Override
  public void beforeUpdate(JPUpdate query) {
    Collection<JPClassHandler> handlers = getHandlers(query.getJpId().getJpClass());
    if (handlers != null) {
      handlers.forEach(x -> x.beforeUpdate(query));
    }
  }

  @Override
  public void beforeDelete(JPDelete query) {
    Collection<JPClassHandler> handlers = getHandlers(query.getJpClass());
    if (handlers != null) {
      handlers.forEach(x -> x.beforeDelete(query));
    }
  }

  @Override
  public boolean useCustomDelete(JPDelete query) {
    boolean result = false;
    Collection<JPClassHandler> handlers = getHandlers(query.getJpClass());
    if (handlers != null) {
      for (JPClassHandler handler : handlers) {
        result = result || handler.useCustomDelete(query);
      }
    }
    return result;
  }

  @Override
  public void customDelete(JPDelete query) {
    Collection<JPClassHandler> handlers = getHandlers(query.getJpClass());
    if (handlers != null) {
      handlers.forEach(x -> x.customDelete(query));
    }
  }

  @Override
  public void afterCreate(Comparable objectId, JPCreate query) {
    Collection<JPClassHandler> handlers = getHandlers(query.getJpClass());
    if (handlers != null) {
      handlers.forEach(x -> x.afterCreate(objectId, query));
    }
  }

  @Override
  public void afterUpdate(JPUpdate query) {
    Collection<JPClassHandler> handlers = getHandlers(query.getJpId().getJpClass());
    if (handlers != null) {
      handlers.forEach(x -> x.afterUpdate(query));
    }
  }

  @Override
  public void afterDelete(JPDelete query) {
    Collection<JPClassHandler> handlers = getHandlers(query.getJpId().getJpClass());
    if (handlers != null) {
      handlers.forEach(x -> x.afterDelete(query));
    }
  }

  @Override
  public void afterCommitCreate(Comparable objectId, JPCreate query) {
    Collection<JPClassHandler> handlers = getHandlers(query.getJpClass());
    if (handlers != null) {
      handlers.forEach(x -> x.afterCommitCreate(objectId, query));
    }
  }

  @Override
  public void afterCommitUpdate(JPUpdate query) {
    Collection<JPClassHandler> handlers = getHandlers(query.getJpId().getJpClass());
    if (handlers != null) {
      handlers.forEach(x -> x.afterCommitUpdate(query));
    }
  }

  @Override
  public void afterCommitDelete(JPDelete query) {
    Collection<JPClassHandler> handlers = getHandlers(query.getJpId().getJpClass());
    if (handlers != null) {
      handlers.forEach(x -> x.afterCommitDelete(query));
    }
  }
}
