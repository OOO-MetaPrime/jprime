package mp.jprime.dataaccess.services;

import mp.jprime.annotations.ClassesLink;
import mp.jprime.dataaccess.JPObjectRepository;
import mp.jprime.dataaccess.JPObjectRepositoryService;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.params.*;
import mp.jprime.exceptions.JPClassMapNotFoundException;
import mp.jprime.exceptions.JPClassNotFoundException;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.metamaps.JPClassMap;
import mp.jprime.metamaps.services.JPMapsStorage;
import mp.jprime.repositories.JPObjectStorage;
import mp.jprime.repositories.JPStorage;
import mp.jprime.repositories.exceptions.JPRepositoryNotFoundException;
import mp.jprime.repositories.services.RepositoryStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JPObjectRepositoryBaseService implements JPObjectRepositoryService {
  private static final Logger LOG = LoggerFactory.getLogger(JPObjectRepositoryBaseService.class);

  /**
   * Описания хранилищ
   */
  private RepositoryStorage repoStorage;
  /**
   * Хранилище метаинформации
   */
  private JPMetaStorage metaStorage;
  /**
   * Описания привязки метаинформации к хранилищу
   */
  private JPMapsStorage mapsStorage;
  /**
   * Обработчики типов
   */
  private Map<Class, JPObjectRepository> repoMap = new ConcurrentHashMap<>();

  /**
   * Считываем аннотации
   */
  @Autowired(required = false)
  private void setRepos(Collection<JPObjectRepository> repos) {
    if (repos == null) {
      return;
    }
    for (JPObjectRepository repo : repos) {
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
  private void setMetaStorage(JPMetaStorage metaStorage) {
    this.metaStorage = metaStorage;
  }

  @Autowired
  private void setMapsStorage(JPMapsStorage mapsStorage) {
    this.mapsStorage = mapsStorage;
  }

  @Autowired
  private void setRepoStorage(RepositoryStorage repoStorage) {
    this.repoStorage = repoStorage;
  }

  private JPObjectRepository getRepository(String classCode) {
    try {
      JPStorage storage = getJpStorage(classCode);
      if (!(storage instanceof JPObjectStorage)) {
        throw new JPClassMapNotFoundException(classCode);
      }
      Class storageClass = storage.getClass();
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

  private JPStorage getJpStorage(String classCode) {
    JPClass jpClass = metaStorage.getJPClassByCode(classCode);
    if (jpClass == null) {
      throw new JPClassNotFoundException(classCode);
    }
    // Получаем маппинг класса
    JPClassMap jpClassMap = mapsStorage.get(jpClass);
    if (jpClassMap == null) {
      throw new JPClassMapNotFoundException(classCode);
    }
    return repoStorage.getStorage(jpClassMap.getStorage());
  }

  @Override
  public Optional<String> getStorageCode(String classCode) {
    JPStorage jpStorage = getJpStorage(classCode);
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

  /**
   * Возвращает объект и блокирует его на время транзакции
   *
   * @param query Параметры для выборки
   * @return Объект
   */
  @Override
  public JPObject getObjectAndLock(JPSelect query) {
    return getRepository(query.getJpClass()).getObjectAndLock(query);
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
  public Mono<JPId> asyncCreate(JPCreate query) {
    return getRepository(query.getJpClass()).asyncCreate(query);
  }

  @Override
  public JPId create(JPCreate query) {
    return getRepository(query.getJpClass()).create(query);
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
  public Mono<JPObject> asyncUpdateAndGet(JPUpdate query) {
    return getRepository(query.getJpClass()).asyncUpdateAndGet(query);
  }

  @Override
  public JPObject updateAndGet(JPUpdate query) {
    return getRepository(query.getJpClass()).updateAndGet(query);
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
  public Mono<Void> asyncBatch(JPBatchCreate query) {
    return getRepository(query.getJpClass()).asyncBatch(query);
  }

  @Override
  public void batch(JPBatchCreate query) {
    getRepository(query.getJpClass()).batch(query);
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
