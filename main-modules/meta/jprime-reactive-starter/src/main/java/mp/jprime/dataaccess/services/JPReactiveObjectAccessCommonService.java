package mp.jprime.dataaccess.services;

import mp.jprime.dataaccess.JPReactiveObjectAccessService;
import mp.jprime.dataaccess.JPReactiveObjectRepositoryService;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.lang.JPMap;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPMeta;
import mp.jprime.reactor.core.publisher.JPMono;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.services.JPResourceAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Реализация проверки доступа к объекту
 */
@Service
public final class JPReactiveObjectAccessCommonService extends JPObjectAccessBaseService
    implements JPReactiveObjectAccessService {
  // Интерфейс создания/изменения объекта
  private final JPReactiveObjectRepositoryService repo;

  private JPReactiveObjectAccessCommonService(@Autowired JPReactiveObjectRepositoryService repo) {
    this.repo = repo;
  }

  @Override
  public Mono<Boolean> checkCreate(String classCode, AuthInfo auth) {
    return JPMono.fromCallable(() -> isCreateCheck(classCode, null, auth));
  }

  @Override
  public Mono<Boolean> checkCreate(String classCode, String refAttrCode, Comparable value, AuthInfo auth) {
    return JPMono.fromCallable(() -> isCreateCheck(classCode, JPMutableData.of(refAttrCode, value), auth));
  }

  @Override
  public Mono<Boolean> checkCreate(String classCode, JPMap createData, AuthInfo auth) {
    return JPMono.fromCallable(() -> isCreateCheck(classCode, createData, auth));
  }

  @Override
  public Mono<Boolean> checkRead(String classCode, AuthInfo auth) {
    return JPMono.fromCallable(() -> isReadCheck(classCode, auth));
  }

  @Override
  public Mono<Boolean> checkRead(JPId id, AuthInfo auth) {
    return checkRead(id, Boolean.FALSE, auth);
  }

  @Override
  public Mono<Boolean> checkDelete(String classCode, AuthInfo auth) {
    return JPMono.fromCallable(() -> isDeleteCheck(classCode, auth));
  }

  @Override
  public Mono<Boolean> checkDelete(JPId id, AuthInfo auth) {
    return checkDelete(id, Boolean.FALSE, auth);
  }

  @Override
  public Mono<Boolean> checkUpdate(String classCode, AuthInfo auth) {
    return JPMono.fromCallable(() -> isUpdateCheck(classCode, auth));
  }

  @Override
  public Mono<Boolean> checkUpdate(JPId id, AuthInfo auth) {
    return checkUpdate(id, null, Boolean.FALSE, auth);
  }

  @Override
  public Mono<Boolean> checkUpdate(JPId id, JPMap updateData, AuthInfo auth) {
    return checkUpdate(id, updateData, Boolean.FALSE, auth);
  }

  @Override
  public Mono<Boolean> checkReadExists(JPId id, AuthInfo auth) {
    return checkRead(id, Boolean.TRUE, auth);
  }

  @Override
  public Mono<Boolean> checkDeleteExists(JPId id, AuthInfo auth) {
    return checkDelete(id, Boolean.TRUE, auth);
  }

  @Override
  public Mono<Boolean> checkUpdateExists(JPId id, AuthInfo auth) {
    return checkUpdate(id, null, Boolean.TRUE, auth);
  }

  @Override
  public Mono<Boolean> checkUpdateExists(JPId id, JPMap updateData, AuthInfo auth) {
    return checkUpdate(id, updateData, Boolean.TRUE, auth);
  }

  private Mono<Boolean> checkRead(JPId id, boolean checkExists, AuthInfo auth) {
    if (id == null) {
      return Mono.just(false);
    }
    JPClass jpClass = getMetaStorage().getJPClassByCode(id.getJpClass());
    if (jpClass == null) {
      return Mono.just(false);
    }
    JPResourceAccess access = getAccessService().checkRead(id.getJpClass(), auth);
    if (!access.isAccess()) {
      return Mono.just(false);
    }
    // доступ к объекту
    if (checkExists || jpClass.hasAttr(JPMeta.Attr.JPPACKAGE) || access.getFilter() != null) {
      return getObject(id, jpClass, access, auth)
          .map(object -> getSecurityStorage().checkRead(object.getJpPackage(), auth.getRoles()))
          .defaultIfEmpty(false);
    }
    return Mono.just(true);
  }

  private Mono<Boolean> checkDelete(JPId id, boolean checkExists, AuthInfo auth) {
    if (id == null || auth == null) {
      return Mono.just(false);
    }
    JPClass jpClass = getMetaStorage().getJPClassByCode(id.getJpClass());
    if (jpClass == null) {
      return Mono.just(false);
    }
    JPResourceAccess access = getAccessService().checkDelete(id.getJpClass(), auth);
    if (!access.isAccess()) {
      return Mono.just(false);
    }
    // доступ к объекту
    if (checkExists || jpClass.hasAttr(JPMeta.Attr.JPPACKAGE) || access.getFilter() != null) {
      return getObject(id, jpClass, access, auth)
          .map(object -> getSecurityStorage().checkDelete(object.getJpPackage(), auth.getRoles()))
          .defaultIfEmpty(false);
    }
    return Mono.just(true);
  }

  private Mono<Boolean> checkUpdate(JPId id, JPMap updateData, boolean checkExists, AuthInfo auth) {
    if (id == null || auth == null) {
      return Mono.just(false);
    }
    JPClass jpClass = getMetaStorage().getJPClassByCode(id.getJpClass());
    if (jpClass == null) {
      return Mono.just(false);
    }
    JPResourceAccess access = getAccessService().checkUpdate(id.getJpClass(), auth);
    if (!access.isAccess()) {
      return Mono.just(false);
    }
    Filter accessFilter = access.getFilter();

    Mono<Boolean> result = Mono.just(true);
    // проверки на значение
    if (updateData != null && accessFilter != null) {
      result = result.map(x -> x && checkData(accessFilter, updateData, auth, true));
    }
    // доступ к объекту
    if (checkExists || jpClass.hasAttr(JPMeta.Attr.JPPACKAGE) || accessFilter != null) {
      result = result.flatMap(x -> !x ? Mono.just(Boolean.FALSE) : getObject(id, jpClass, access, auth)
          .map(object -> getSecurityStorage().checkUpdate(object.getJpPackage(), auth.getRoles()))
          .defaultIfEmpty(false)
      );
    }
    return result;
  }

  private Mono<JPObject> getObject(JPId id, JPClass jpClass, JPResourceAccess access, AuthInfo auth) {
    return repo.getAsyncObject(
        toSelect(id, jpClass, access, auth)
    );
  }
}