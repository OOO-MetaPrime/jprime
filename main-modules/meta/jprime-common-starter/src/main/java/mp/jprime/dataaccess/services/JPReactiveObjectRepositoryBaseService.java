package mp.jprime.dataaccess.services;

import mp.jprime.annotations.ClassesLink;
import mp.jprime.dataaccess.JPReactiveObjectRepository;
import mp.jprime.dataaccess.JPReactiveObjectRepositoryService;
import mp.jprime.dataaccess.JPReactiveObjectRepositoryServiceAware;
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
import mp.jprime.repositories.services.RepositoryStorage;
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
public class JPReactiveObjectRepositoryBaseService implements JPReactiveObjectRepositoryService {
  private static final Logger LOG = LoggerFactory.getLogger(JPReactiveObjectRepositoryBaseService.class);

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
  private Map<Class, JPReactiveObjectRepository> repoMap = new ConcurrentHashMap<>();

  /**
   * Указание ссылок
   */
  @Autowired(required = false)
  private void setAwares(Collection<JPReactiveObjectRepositoryServiceAware> awares) {
    for (JPReactiveObjectRepositoryServiceAware aware : awares) {
      aware.setJpReactiveObjectRepositoryService(this);
    }
  }

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

  private JPReactiveObjectRepository getRepository(String classCode) {
    try {
      JPClass jpClass = metaStorage.getJPClassByCode(classCode);
      if (jpClass == null) {
        throw new JPClassNotFoundException(classCode);
      }
      // Получаем маппинг класса
      JPClassMap jpClassMap = mapsStorage.get(jpClass);
      if (jpClassMap == null) {
        throw new JPClassMapNotFoundException(classCode);
      }
      JPStorage storage = repoStorage.getStorage(jpClassMap.getStorage());
      if (!(storage instanceof JPObjectStorage)) {
        throw new JPClassMapNotFoundException(classCode);
      }
      Class storageClass = storage.getClass();
      JPReactiveObjectRepository rep = null;
      while (rep == null && storageClass != null) {
        rep = repoMap.get(storageClass);
        storageClass = storageClass.getSuperclass();
      }
      return rep;
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
  public Mono<Long> asyncDelete(JPDelete query) {
    return getRepository(query.getJpClass()).asyncDelete(query);
  }
}
