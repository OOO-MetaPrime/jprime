package mp.jprime.dataaccess.uniquevalues.services;

import mp.jprime.annotations.ClassesLink;
import mp.jprime.annotations.JPClassesLink;
import mp.jprime.dataaccess.beans.JPUniqueValue;
import mp.jprime.dataaccess.params.JPSelect;
import mp.jprime.dataaccess.uniquevalues.JPUniqueValuesHandler;
import mp.jprime.dataaccess.uniquevalues.JPUniqueValuesRepository;
import mp.jprime.dataaccess.uniquevalues.JPUniqueValuesService;
import mp.jprime.exceptions.JPClassMapNotFoundException;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.repositories.JPObjectStorage;
import mp.jprime.repositories.JPStorage;
import mp.jprime.metastorage.JPMetaStorageService;
import mp.jprime.repositories.exceptions.JPRepositoryNotFoundException;
import mp.jprime.utils.JPApplicationShutdownManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Базовая логика JPUniqueValuesService
 */
@Service
public final class JPUniqueValuesCommonService implements JPUniqueValuesService {
  private static final Logger LOG = LoggerFactory.getLogger(JPUniqueValuesCommonService.class);

  private JPApplicationShutdownManager shutdownManager;

  private final Map<Class, JPUniqueValuesRepository> repoMap = new ConcurrentHashMap<>();
  private Map<String, JPUniqueValuesHandler> jpHandlers = new HashMap<>();

  private JPMetaStorageService storageService;

  @Autowired
  private void setShutdownManager(JPApplicationShutdownManager shutdownManager) {
    this.shutdownManager = shutdownManager;
  }

  /**
   * Считываем аннотации
   */
  @Autowired(required = false)
  private void setRepos(Collection<JPUniqueValuesRepository> repos) {
    if (repos == null) {
      return;
    }
    for (JPUniqueValuesRepository repo : repos) {
      try {
        ClassesLink anno = repo.getClass().getAnnotation(ClassesLink.class);
        if (anno == null) {
          continue;
        }
        for (Class javaClass : anno.classes()) {
          if (javaClass == null) {
            continue;
          }
          repoMap.put(javaClass, repo);
        }
      } catch (Exception e) {
        throw JPRuntimeException.wrapException(e);
      }
    }
  }

  @Autowired
  private void setStorageService(JPMetaStorageService storageService) {
    this.storageService = storageService;
  }

  /**
   * Считываем аннотации
   */
  @Autowired(required = false)
  private void setHandlers(Collection<JPUniqueValuesHandler> handlers) {
    if (handlers == null) {
      return;
    }
    Map<String, JPUniqueValuesHandler> jpHandlers = new HashMap<>();
    for (JPUniqueValuesHandler handler : handlers) {
      try {
        JPClassesLink anno = handler.getClass().getAnnotation(JPClassesLink.class);
        if (anno == null) {
          continue;
        }
        for (String jpClassCode : anno.jpClasses()) {
          if (jpClassCode == null || jpClassCode.isEmpty()) {
            continue;
          }
          if (jpHandlers.containsKey(jpClassCode)) {
            LOG.error("Error duplication JPUniqueValuesHandler for jpClass {}", jpClassCode);
            shutdownManager.exitWithError();
          }
          jpHandlers.put(jpClassCode, handler);
        }
      } catch (Exception e) {
        throw JPRuntimeException.wrapException(e);
      }
    }
    this.jpHandlers = jpHandlers;
  }


  private JPUniqueValuesRepository getRepository(String classCode) {
    try {
      JPStorage storage = storageService.getJpStorage(classCode);
      if (!(storage instanceof JPObjectStorage)) {
        throw new JPClassMapNotFoundException(classCode);
      }
      Class storageClass = storage.getClass();
      JPUniqueValuesRepository rep = null;
      while (rep == null && storageClass != null) {
        rep = repoMap.get(storageClass);
        storageClass = storageClass.getSuperclass();
      }
      if (rep != null) {
        return rep;
      } else {
        throw new JPRepositoryNotFoundException("JPUniqueValuesRepository", storage.getCode());
      }
    } catch (JPRuntimeException e) {
      LOG.error(e.getMessage(), e);
      throw e;
    }
  }

  @Override
  public Collection<JPUniqueValue> getUniqueValues(JPSelect select, List<String> hierarchy) {
    String jpClassCode = select.getJpClass();
    JPUniqueValuesHandler handler = jpHandlers.get(jpClassCode);
    if (handler != null) {
      return handler.getUniqueValues(select, hierarchy);
    }
    return getRepository(jpClassCode).getUniqueValues(select, hierarchy);
  }
}
