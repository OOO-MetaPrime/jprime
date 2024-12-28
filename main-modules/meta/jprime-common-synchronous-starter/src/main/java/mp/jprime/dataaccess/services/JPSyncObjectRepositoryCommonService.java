package mp.jprime.dataaccess.services;

import mp.jprime.annotations.ClassesLink;
import mp.jprime.dataaccess.JPSyncObjectRepository;
import mp.jprime.dataaccess.JPSyncObjectRepositoryService;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.params.*;
import mp.jprime.exceptions.JPClassMapNotFoundException;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.metastorage.JPMetaStorageService;
import mp.jprime.repositories.JPObjectStorage;
import mp.jprime.repositories.JPStorage;
import mp.jprime.repositories.exceptions.JPRepositoryNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public final class JPSyncObjectRepositoryCommonService implements JPSyncObjectRepositoryService {
  private static final Logger LOG = LoggerFactory.getLogger(JPSyncObjectRepositoryCommonService.class);

  private final Map<Class, JPSyncObjectRepository> repoMap = new ConcurrentHashMap<>();

  private JPMetaStorageService storageService;

  /**
   * Считываем аннотации
   */
  @Autowired(required = false)
  private void setRepos(Collection<JPSyncObjectRepository> repos) {
    if (repos == null) {
      return;
    }
    for (JPSyncObjectRepository repo : repos) {
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

  private JPSyncObjectRepository getRepository(String classCode) {
    try {
      JPStorage storage = storageService.getJpStorage(classCode);
      if (!(storage instanceof JPObjectStorage)) {
        throw new JPClassMapNotFoundException(classCode);
      }
      Class storageClass = storage.getClass();
      JPSyncObjectRepository rep = null;
      while (rep == null && storageClass != null) {
        rep = repoMap.get(storageClass);
        storageClass = storageClass.getSuperclass();
      }
      if (rep != null) {
        return rep;
      } else {
        throw new JPRepositoryNotFoundException("JPSyncObjectRepository", storage.getCode());
      }
    } catch (JPRuntimeException e) {
      LOG.error(e.getMessage(), e);
      throw e;
    }
  }

  @Override
  public Optional<String> getStorageCode(String classCode) {
    JPStorage jpStorage = storageService.getJpStorage(classCode);
    if (jpStorage != null) {
      return Optional.of(jpStorage.getCode());
    }
    return Optional.empty();
  }

  @Override
  public JPObject getObject(JPSelect query) {
    return getRepository(query.getJpClass()).getObject(query);
  }

  @Override
  public JPObject getObjectAndLock(JPSelect query) {
    return getRepository(query.getJpClass()).getObjectAndLock(query);
  }

  @Override
  public JPObject getObjectAndLock(JPSelect query, boolean skipLocked) {
    return getRepository(query.getJpClass()).getObjectAndLock(query, skipLocked);
  }

  @Override
  public Collection<JPObject> getList(JPSelect select) {
    return getRepository(select.getJpClass()).getList(select);
  }

  @Override
  public Collection<JPObject> getListAndLock(JPSelect query, boolean skipLocked) {
    return getRepository(query.getJpClass()).getListAndLock(query, skipLocked);
  }

  @Override
  public Collection<JPObject> getListAndLock(JPSelect select) {
    return getRepository(select.getJpClass()).getListAndLock(select);
  }

  @Override
  public Long getTotalCount(JPSelect query) {
    return getRepository(query.getJpClass()).getTotalCount(query);
  }

  @Override
  public JPData getAggregate(JPAggregate query) {
    return getRepository(query.getJpClass()).getAggregate(query);
  }

  @Override
  public JPId create(JPCreate query) {
    return getRepository(query.getJpClass()).create(query);
  }


  @Override
  public JPObject createAndGet(JPCreate query) {
    return getRepository(query.getJpClass()).createAndGet(query);
  }

  @Override
  public JPId update(JPUpdate query) {
    return getRepository(query.getJpClass()).update(query);
  }

  @Override
  public Long update(JPConditionalUpdate query) {
    return getRepository(query.getJpClass()).update(query);
  }

  @Override
  public JPObject updateAndGet(JPUpdate query) {
    return getRepository(query.getJpClass()).updateAndGet(query);
  }

  @Override
  public JPId patch(JPCreate query) {
    return getRepository(query.getJpClass()).patch(query);
  }

  @Override
  public JPObject patchAndGet(JPCreate query) {
    return getRepository(query.getJpClass()).patchAndGet(query);
  }

  @Override
  public Long delete(JPDelete query) {
    return getRepository(query.getJpClass()).delete(query);
  }

  @Override
  public Long delete(JPConditionalDelete query) {
    return getRepository(query.getJpClass()).delete(query);
  }

  @Override
  public void batch(JPBatchCreate query) {
    getRepository(query.getJpClass()).batch(query);
  }

  @Override
  public <T> List<T> batchWithKeys(JPBatchCreate query) {
    return getRepository(query.getJpClass()).batchWithKeys(query);
  }

  @Override
  public void batch(JPBatchUpdate query) {
    getRepository(query.getJpClass()).batch(query);
  }
}
