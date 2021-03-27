package mp.jprime.dataaccess.services;

import mp.jprime.dataaccess.JPReactiveObjectAccessService;
import mp.jprime.dataaccess.JPReactiveObjectRepositoryService;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPMeta;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.services.JPResourceAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Реализация проверки доступа к объекту
 */
@Service
public class JPReactiveObjectAccessCommonService extends JPObjectAccessBaseService implements JPReactiveObjectAccessService {
  // Интерфейс создания/изменения объекта
  private JPReactiveObjectRepositoryService repo;

  @Autowired
  private void setRepo(JPReactiveObjectRepositoryService repo) {
    this.repo = repo;
  }

  /**
   * Проверка доступа на создание
   *
   * @param classCode Код метаописания
   * @param auth      AuthInfo
   * @return Да/Нет
   */
  @Override
  public Mono<Boolean> checkCreate(String classCode, AuthInfo auth) {
    return Mono.fromCallable(() -> isCreateCheck(classCode, auth));
  }

  /**
   * Проверка доступа на чтение
   *
   * @param id   Идентификатор объекта
   * @param auth AuthInfo
   * @return Да/Нет
   */
  @Override
  public Mono<Boolean> checkRead(JPId id, AuthInfo auth) {
    return checkRead(id, Boolean.FALSE, auth);
  }

  /**
   * Проверка доступа на удаление
   *
   * @param id   Идентификатор объекта
   * @param auth AuthInfo
   * @return Да/Нет
   */
  @Override
  public Mono<Boolean> checkDelete(JPId id, AuthInfo auth) {
    return checkDelete(id, Boolean.FALSE, auth);
  }

  /**
   * Проверка доступа на обновление
   *
   * @param id   Идентификатор объекта
   * @param auth AuthInfo
   * @return Да/Нет
   */
  @Override
  public Mono<Boolean> checkUpdate(JPId id, AuthInfo auth) {
    return checkUpdate(id, Boolean.FALSE, auth);
  }

  /**
   * Проверка доступа на чтение + наличие объекта
   *
   * @param id   Идентификатор объекта
   * @param auth AuthInfo
   * @return Да/Нет
   */
  @Override
  public Mono<Boolean> checkReadExists(JPId id, AuthInfo auth) {
    return checkRead(id, Boolean.TRUE, auth);
  }

  /**
   * Проверка доступа на удаление
   *
   * @param id   Идентификатор объекта + наличие объекта
   * @param auth AuthInfo
   * @return Да/Нет
   */
  @Override
  public Mono<Boolean> checkDeleteExists(JPId id, AuthInfo auth) {
    return checkDelete(id, Boolean.TRUE, auth);
  }

  /**
   * Проверка доступа на обновление
   *
   * @param id   Идентификатор объекта + наличие объекта
   * @param auth AuthInfo
   * @return Да/Нет
   */
  @Override
  public Mono<Boolean> checkUpdateExists(JPId id, AuthInfo auth) {
    return checkUpdate(id, Boolean.TRUE, auth);
  }


  private Mono<Boolean> checkRead(JPId id, boolean checkExists, AuthInfo auth) {
    if (id == null) {
      return Mono.just(false);
    }
    JPClass jpClass = metaStorage.getJPClassByCode(id.getJpClass());
    if (jpClass == null) {
      return Mono.just(false);
    }
    JPResourceAccess access = accessService.checkRead(id.getJpClass(), auth);
    if (!access.isAccess()) {
      return Mono.just(false);
    }
    // доступ к объекту
    if (checkExists || jpClass.hasAttr(JPMeta.Attr.JPPACKAGE)) {
      return getObject(id, jpClass, access, auth)
          .map(object -> securityManager.checkRead(object.getJpPackage(), auth.getRoles()))
          .defaultIfEmpty(false);
    }
    return Mono.just(true);
  }

  private Mono<Boolean> checkDelete(JPId id, boolean checkExists, AuthInfo auth) {
    if (id == null || auth == null) {
      return Mono.just(false);
    }
    JPClass jpClass = metaStorage.getJPClassByCode(id.getJpClass());
    if (jpClass == null) {
      return Mono.just(false);
    }
    JPResourceAccess access = accessService.checkDelete(id.getJpClass(), auth);
    if (!access.isAccess()) {
      return Mono.just(false);
    }
    // доступ к объекту
    if (checkExists || jpClass.hasAttr(JPMeta.Attr.JPPACKAGE)) {
      return getObject(id, jpClass, access, auth)
          .map(object -> securityManager.checkDelete(object.getJpPackage(), auth.getRoles()))
          .defaultIfEmpty(false);
    }
    return Mono.just(true);
  }

  private Mono<Boolean> checkUpdate(JPId id, boolean checkExists, AuthInfo auth) {
    if (id == null || auth == null) {
      return Mono.just(false);
    }
    JPClass jpClass = metaStorage.getJPClassByCode(id.getJpClass());
    if (jpClass == null) {
      return Mono.just(false);
    }
    JPResourceAccess access = accessService.checkUpdate(id.getJpClass(), auth);
    if (!access.isAccess()) {
      return Mono.just(false);
    }
    // доступ к объекту
    if (checkExists || jpClass.hasAttr(JPMeta.Attr.JPPACKAGE)) {
      return getObject(id, jpClass, access, auth)
          .map(object -> securityManager.checkUpdate(object.getJpPackage(), auth.getRoles()))
          .defaultIfEmpty(false);
    }
    return Mono.just(true);
  }

  private Mono<JPObject> getObject(JPId id, JPClass jpClass, JPResourceAccess access, AuthInfo auth) {
    return repo.getAsyncObject(
        toSelect(id, jpClass, access, auth)
    );
  }
}