package mp.jprime.dataaccess.services;

import mp.jprime.annotations.ClassesLink;
import mp.jprime.dataaccess.JPObjectRepository;
import mp.jprime.dataaccess.JPObjectRepositoryService;
import mp.jprime.dataaccess.JPSyncObjectRepository;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.params.*;
import mp.jprime.exceptions.JPClassMapNotFoundException;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.repositories.JPObjectStorage;
import mp.jprime.repositories.JPStorage;
import mp.jprime.metastorage.JPMetaStorageService;
import mp.jprime.repositories.exceptions.JPRepositoryNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Service
public final class JPObjectRepositoryCommonService implements JPObjectRepositoryService {
  private static final Logger LOG = LoggerFactory.getLogger(JPObjectRepositoryCommonService.class);

  private final Map<Class<?>, JPObjectRepository> repoMap = new ConcurrentHashMap<>();

  private JPMetaStorageService storageService;

  @Autowired(required = false)
  private void setAsyncRepos(Collection<JPObjectRepository> asyncRepos) {
    if (asyncRepos != null) {
      for (JPObjectRepository repo : asyncRepos) {
        fill(repo.getClass(), javaClass -> repoMap.put(javaClass, repo));
      }
    }
  }

  @Autowired(required = false)
  private void setSyncRepos(Collection<JPSyncObjectRepository> syncRepos) {
    if (syncRepos != null) {
      for (JPSyncObjectRepository repo : syncRepos) {
        if (repo instanceof JPObjectRepository) {
          continue;
        }
        JPObjectRepository wrapRepo = JPObjectSyncWrapRepository.of(repo);
        fill(repo.getClass(), javaClass -> repoMap.put(javaClass, wrapRepo));
      }
    }
  }

  private void fill(Class<?> repoClass, Consumer<Class<?>> func) {
    try {
      ClassesLink anno = repoClass.getAnnotation(ClassesLink.class);
      if (anno == null) {
        return;
      }
      for (Class<?> javaClass : anno.classes()) {
        if (javaClass == null) {
          continue;
        }
        func.accept(javaClass);
      }
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e);
    }
  }

  @Autowired
  private void setStorageService(JPMetaStorageService storageService) {
    this.storageService = storageService;
  }

  private JPObjectRepository getRepository(String classCode) {
    try {
      JPStorage storage = storageService.getJpStorage(classCode);
      if (!(storage instanceof JPObjectStorage)) {
        throw new JPClassMapNotFoundException(classCode);
      }
      Class<?> storageClass = storage.getClass();
      JPObjectRepository rep = null;
      while (rep == null && storageClass != null) {
        rep = repoMap.get(storageClass);
        storageClass = storageClass.getSuperclass();
      }
      if (rep != null) {
        return rep;
      } else {
        throw new JPRepositoryNotFoundException("JPObjectRepositoryService", storage.getCode());
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
  public Mono<JPObject> getAsyncObject(JPSelect select) {
    return getRepository(select.getJpClass()).getAsyncObject(select);
  }

  @Override
  public Flux<JPObject> getAsyncList(JPSelect query) {
    return getRepository(query.getJpClass()).getAsyncList(query);
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
  public Mono<Long> getAsyncTotalCount(JPSelect query) {
    return getRepository(query.getJpClass()).getAsyncTotalCount(query);
  }

  @Override
  public Long getTotalCount(JPSelect query) {
    return getRepository(query.getJpClass()).getTotalCount(query);
  }

  @Override
  public Mono<JPData> getAsyncAggregate(JPAggregate query) {
    return getRepository(query.getJpClass()).getAsyncAggregate(query);
  }

  @Override
  public JPData getAggregate(JPAggregate query) {
    return getRepository(query.getJpClass()).getAggregate(query);
  }

  @Override
  public Mono<JPId> asyncCreate(JPCreate query) {
    return getRepository(query.getJpClass()).asyncCreate(query);
  }

  @Override
  public JPId create(JPCreate query) {
    return getRepository(query.getJpClass()).create(query);
  }

  @Override
  public Mono<JPObject> asyncCreateAndGet(JPCreate query) {
    return getRepository(query.getJpClass()).asyncCreateAndGet(query);
  }

  @Override
  public JPObject createAndGet(JPCreate query) {
    return getRepository(query.getJpClass()).createAndGet(query);
  }

  @Override
  public Mono<JPId> asyncUpdate(JPUpdate query) {
    return getRepository(query.getJpClass()).asyncUpdate(query);
  }

  @Override
  public JPId update(JPUpdate query) {
    return getRepository(query.getJpClass()).update(query);
  }

  @Override
  public Mono<Long> asyncUpdate(JPConditionalUpdate query) {
    return getRepository(query.getJpClass()).asyncUpdate(query);
  }

  @Override
  public Long update(JPConditionalUpdate query) {
    return getRepository(query.getJpClass()).update(query);
  }

  @Override
  public Mono<JPObject> asyncUpdateAndGet(JPUpdate query) {
    return getRepository(query.getJpClass()).asyncUpdateAndGet(query);
  }

  @Override
  public JPObject updateAndGet(JPUpdate query) {
    return getRepository(query.getJpClass()).updateAndGet(query);
  }

  @Override
  public Mono<JPId> asyncPatch(JPCreate query) {
    return getRepository(query.getJpClass()).asyncPatch(query);
  }

  @Override
  public JPId patch(JPCreate query) {
    return getRepository(query.getJpClass()).patch(query);
  }

  @Override
  public Mono<JPObject> asyncPatchAndGet(JPCreate query) {
    return getRepository(query.getJpClass()).asyncPatchAndGet(query);
  }

  @Override
  public JPObject patchAndGet(JPCreate query) {
    return getRepository(query.getJpClass()).patchAndGet(query);
  }

  @Override
  public Mono<Long> asyncDelete(JPDelete query) {
    return getRepository(query.getJpClass()).asyncDelete(query);
  }

  @Override
  public Long delete(JPDelete query) {
    return getRepository(query.getJpClass()).delete(query);
  }

  @Override
  public Mono<Long> asyncDelete(JPConditionalDelete query) {
    return getRepository(query.getJpClass()).asyncDelete(query);
  }

  @Override
  public Long delete(JPConditionalDelete query) {
    return getRepository(query.getJpClass()).delete(query);
  }

  @Override
  public Mono<Void> asyncBatch(JPBatchCreate query) {
    return getRepository(query.getJpClass()).asyncBatch(query);
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
  public Mono<Void> asyncBatch(JPBatchUpdate query) {
    return getRepository(query.getJpClass()).asyncBatch(query);
  }

  @Override
  public void batch(JPBatchUpdate query) {
    getRepository(query.getJpClass()).batch(query);
  }

  @Override
  public Mono<JPObject> getAsyncObjectAndLock(JPSelect query) {
    return getRepository(query.getJpClass()).getAsyncObjectAndLock(query);
  }

  @Override
  public Flux<JPObject> getAsyncListAndLock(JPSelect query) {
    return getRepository(query.getJpClass()).getAsyncListAndLock(query);
  }
}
