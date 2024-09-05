package mp.jprime.dataaccess.services;

import mp.jprime.annotations.ClassesLink;
import mp.jprime.dataaccess.JPReactiveObjectRepository;
import mp.jprime.dataaccess.JPReactiveObjectRepositoryService;
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public final class JPReactiveObjectRepositoryCommonService implements JPReactiveObjectRepositoryService {
  private static final Logger LOG = LoggerFactory.getLogger(JPReactiveObjectRepositoryCommonService.class);

  private final Map<Class, JPReactiveObjectRepository> repoMap = new ConcurrentHashMap<>();

  private JPMetaStorageService storageService;

  /**
   * Считываем аннотации
   */
  @Autowired(required = false)
  private void setRepos(Collection<JPReactiveObjectRepository> repos) {
    if (repos == null) {
      return;
    }
    for (JPReactiveObjectRepository repo : repos) {
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

  private JPReactiveObjectRepository getRepository(String classCode) {
    try {
      JPStorage storage = storageService.getJpStorage(classCode);
      if (!(storage instanceof JPObjectStorage)) {
        throw new JPClassMapNotFoundException(classCode);
      }
      Class storageClass = storage.getClass();
      JPReactiveObjectRepository rep = null;
      while (rep == null && storageClass != null) {
        rep = repoMap.get(storageClass);
        storageClass = storageClass.getSuperclass();
      }
      if (rep != null) {
        return rep;
      } else {
        throw new JPRepositoryNotFoundException("JPReactiveObjectRepository", storage.getCode());
      }
    } catch (JPRuntimeException e) {
      LOG.error(e.getMessage(), e);
      throw e;
    }
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
  public Mono<Long> getAsyncTotalCount(JPSelect query) {
    return getRepository(query.getJpClass()).getAsyncTotalCount(query);
  }

  @Override
  public Mono<JPId> asyncCreate(JPCreate query) {
    return getRepository(query.getJpClass()).asyncCreate(query);
  }

  @Override
  public Mono<JPData> getAsyncAggregate(JPAggregate query) {
    return getRepository(query.getJpClass()).getAsyncAggregate(query);
  }

  @Override
  public Mono<JPObject> asyncCreateAndGet(JPCreate query) {
    return getRepository(query.getJpClass()).asyncCreateAndGet(query);
  }

  @Override
  public Mono<JPId> asyncUpdate(JPUpdate query) {
    return getRepository(query.getJpClass()).asyncUpdate(query);
  }

  @Override
  public Mono<JPObject> asyncUpdateAndGet(JPUpdate query) {
    return getRepository(query.getJpClass()).asyncUpdateAndGet(query);
  }

  @Override
  public Mono<JPId> asyncPatch(JPCreate query) {
    return getRepository(query.getJpClass()).asyncPatch(query);
  }

  @Override
  public Mono<JPObject> asyncPatchAndGet(JPCreate query) {
    return getRepository(query.getJpClass()).asyncPatchAndGet(query);
  }

  @Override
  public Mono<Long> asyncDelete(JPDelete query) {
    return getRepository(query.getJpClass()).asyncDelete(query);
  }
}
