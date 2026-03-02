package mp.jprime.dataaccess.generated.services;

import mp.jprime.annotations.ClassesLink;
import mp.jprime.annotations.JPClassesLink;
import mp.jprime.dataaccess.JPObjectRepository;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.generated.GeneratedJPClassStorage;
import mp.jprime.dataaccess.generated.repositories.GeneratedStorage;
import mp.jprime.dataaccess.params.*;
import mp.jprime.exceptions.JPClassMapNotFoundException;
import mp.jprime.exceptions.JPRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
@ClassesLink(classes = {GeneratedStorage.class})
public final class GeneratedRepository implements JPObjectRepository {
  private Map<String, GeneratedJPClassStorage> jpClassStorages = new HashMap<>();

  private GeneratedRepository(@Autowired(required = false) Collection<GeneratedJPClassStorage> storages) {
    if (storages == null) {
      return;
    }
    Map<String, GeneratedJPClassStorage> jpClassStorages = new HashMap<>();
    for (GeneratedJPClassStorage storage : storages) {
      try {
        JPClassesLink anno = storage.getClass().getAnnotation(JPClassesLink.class);
        if (anno == null) {
          continue;
        }
        for (String jpClassCode : anno.jpClasses()) {
          if (jpClassCode == null || jpClassCode.isEmpty()) {
            continue;
          }
          jpClassStorages.put(jpClassCode, storage);
        }
      } catch (Exception e) {
        throw JPRuntimeException.wrapException(e);
      }
    }
    this.jpClassStorages = jpClassStorages;
  }

  /**
   * Хранилище
   *
   * @param jpClassCode Кодовое имя класс
   * @return Хранилище
   */
  private GeneratedJPClassStorage getStorage(String jpClassCode) {
    GeneratedJPClassStorage storage = jpClassCode != null ? jpClassStorages.get(jpClassCode) : null;
    if (storage == null) {
      throw new JPClassMapNotFoundException(jpClassCode);
    }
    return storage;
  }

  @Override
  public Mono<JPObject> getAsyncObject(JPSelect select) {
    return getStorage(select.getJpClass()).getAsyncObject(select);
  }

  @Override
  public Mono<JPObject> getAsyncObjectAndLock(JPSelect select) {
    return getStorage(select.getJpClass()).getAsyncObjectAndLock(select);
  }

  @Override
  public JPObject getObject(JPSelect select) {
    return getStorage(select.getJpClass()).getObject(select);
  }

  @Override
  public JPObject getObjectAndLock(JPSelect select) {
    return getStorage(select.getJpClass()).getObjectAndLock(select);
  }

  @Override
  public JPObject getObjectAndLock(JPSelect select, boolean skipLocked) {
    return getStorage(select.getJpClass()).getObjectAndLock(select, skipLocked);
  }

  @Override
  public Mono<Long> getAsyncTotalCount(JPSelect select) {
    return getStorage(select.getJpClass()).getAsyncTotalCount(select);
  }

  @Override
  public Long getTotalCount(JPSelect select) {
    return getStorage(select.getJpClass()).getTotalCount(select);
  }

  @Override
  public Flux<JPObject> getAsyncList(JPSelect select) {
    return getStorage(select.getJpClass()).getAsyncList(select);
  }

  @Override
  public Flux<JPObject> getAsyncListAndLock(JPSelect select) {
    return getStorage(select.getJpClass()).getAsyncListAndLock(select);
  }

  @Override
  public Collection<JPObject> getList(JPSelect select) {
    return getStorage(select.getJpClass()).getList(select);
  }

  @Override
  public Collection<JPObject> getListAndLock(JPSelect select) {
    return getStorage(select.getJpClass()).getListAndLock(select);
  }

  @Override
  public Collection<JPObject> getListAndLock(JPSelect select, boolean skipLocked) {
    return getStorage(select.getJpClass()).getListAndLock(select, skipLocked);
  }

  @Override
  public Mono<JPData> getAsyncAggregate(JPAggregate aggr) {
    return getStorage(aggr.getJpClass()).getAsyncAggregate(aggr);
  }

  @Override
  public JPData getAggregate(JPAggregate aggr) {
    return getStorage(aggr.getJpClass()).getAggregate(aggr);
  }

  @Override
  public Mono<JPId> asyncCreate(JPCreate query) {
    return getStorage(query.getJpClass()).asyncCreate(query);
  }

  @Override
  public JPId create(JPCreate query) {
    return getStorage(query.getJpClass()).create(query);
  }

  @Override
  public Mono<JPObject> asyncCreateAndGet(JPCreate query) {
    return getStorage(query.getJpClass()).asyncCreateAndGet(query);
  }

  @Override
  public JPObject createAndGet(JPCreate query) {
    return getStorage(query.getJpClass()).createAndGet(query);
  }

  @Override
  public Mono<JPId> asyncUpdate(JPUpdate query) {
    return getStorage(query.getJpClass()).asyncUpdate(query);
  }

  @Override
  public Mono<Long> asyncUpdate(JPConditionalUpdate query) {
    return getStorage(query.getJpClass()).asyncUpdate(query);
  }

  @Override
  public JPId update(JPUpdate query) {
    return getStorage(query.getJpClass()).update(query);
  }

  @Override
  public Long update(JPConditionalUpdate query) {
    return getStorage(query.getJpClass()).update(query);
  }

  @Override
  public Mono<JPObject> asyncUpdateAndGet(JPUpdate query) {
    return getStorage(query.getJpClass()).asyncUpdateAndGet(query);
  }

  @Override
  public JPObject updateAndGet(JPUpdate query) {
    return getStorage(query.getJpClass()).updateAndGet(query);
  }

  @Override
  public Mono<JPId> asyncPatch(JPCreate query) {
    return getStorage(query.getJpClass()).asyncPatch(query);
  }

  @Override
  public JPId patch(JPCreate query) {
    return getStorage(query.getJpClass()).patch(query);
  }

  @Override
  public Mono<JPObject> asyncPatchAndGet(JPCreate query) {
    return getStorage(query.getJpClass()).asyncPatchAndGet(query);
  }

  @Override
  public JPObject patchAndGet(JPCreate query) {
    return getStorage(query.getJpClass()).patchAndGet(query);
  }

  @Override
  public Mono<Long> asyncDelete(JPDelete query) {
    return getStorage(query.getJpClass()).asyncDelete(query);
  }

  @Override
  public Long delete(JPDelete query) {
    return getStorage(query.getJpClass()).delete(query);
  }

  @Override
  public Mono<Long> asyncDelete(JPConditionalDelete query) {
    return getStorage(query.getJpClass()).asyncDelete(query);
  }

  @Override
  public Long delete(JPConditionalDelete query) {
    return getStorage(query.getJpClass()).delete(query);
  }
}