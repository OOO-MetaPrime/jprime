package mp.jprime.dataaccess.services;

import mp.jprime.dataaccess.JPReactiveObjectAccessService;
import mp.jprime.dataaccess.JPReactiveObjectRepositoryService;
import mp.jprime.dataaccess.JPReactiveObjectRepositoryServiceAware;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPMeta;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.services.JPResourceAccess;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Реализация проверки доступа к объекту
 */
@Service
public class JPReactiveObjectAccessCommonService extends JPObjectAccessBaseService
    implements JPReactiveObjectAccessService, JPReactiveObjectRepositoryServiceAware {
  // Интерфейс создания/изменения объекта
  private JPReactiveObjectRepositoryService repo;

  @Override
  public void setJpReactiveObjectRepositoryService(JPReactiveObjectRepositoryService repo) {
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
    return Mono.fromCallable(() -> isCreateCheck(classCode, null, auth));
  }

  /**
   * Проверка доступа на создание из другого объекта
   *
   * @param classCode   Код метаописания
   * @param refAttrCode Ссылочный атрибут
   * @param value       Значение ссылочного атрибута
   * @param auth        AuthInfo
   * @return Да/Нет
   */
  @Override
  public Mono<Boolean> checkCreate(String classCode, String refAttrCode, Comparable value, AuthInfo auth) {
    return Mono.fromCallable(() -> isCreateCheck(classCode, JPMutableData.of(refAttrCode, value), auth));
  }

  /**
   * Проверка доступа на создание
   *
   * @param classCode  Код метаописания
   * @param createData Данные для создания
   * @param auth       AuthInfo
   * @return Да/Нет
   */
  @Override
  public Mono<Boolean> checkCreate(String classCode, JPMutableData createData, AuthInfo auth) {
    return Mono.fromCallable(() -> isCreateCheck(classCode, createData, auth));
  }

  /**
   * Проверка доступа на чтение
   *
   * @param classCode Код метаописания
   * @param auth      AuthInfo
   * @return Да/Нет
   */
  @Override
  public Mono<Boolean> checkRead(String classCode, AuthInfo auth) {
    return Mono.fromCallable(() -> isReadCheck(classCode, auth));
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
   * @param classCode Код метаописания
   * @param auth      AuthInfo
   * @return Да/Нет
   */
  @Override
  public Mono<Boolean> checkDelete(String classCode, AuthInfo auth) {
    return Mono.fromCallable(() -> isDeleteCheck(classCode, auth));
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
   * @param classCode Код метаописания
   * @param auth      AuthInfo
   * @return Да/Нет
   */
  @Override
  public Mono<Boolean> checkUpdate(String classCode, AuthInfo auth) {
    return Mono.fromCallable(() -> isUpdateCheck(classCode, auth));
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
    return checkUpdate(id, null, Boolean.FALSE, auth);
  }

  /**
   * Проверка доступа на обновление
   *
   * @param id         Идентификатор объекта
   * @param updateData Данные для обновления
   * @param auth       AuthInfo
   * @return Да/Нет
   */
  @Override
  public Mono<Boolean> checkUpdate(JPId id, JPMutableData updateData, AuthInfo auth) {
    return checkUpdate(id, updateData, Boolean.FALSE, auth);
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
    return checkUpdate(id, null, Boolean.TRUE, auth);
  }

  /**
   * Проверка доступа на обновление
   *
   * @param id         Идентификатор объекта + наличие объекта
   * @param updateData Данные для обновления
   * @param auth       AuthInfo
   * @return Да/Нет
   */
  @Override
  public Mono<Boolean> checkUpdateExists(JPId id, JPMutableData updateData, AuthInfo auth) {
    return checkUpdate(id, updateData, Boolean.TRUE, auth);
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
    if (checkExists || jpClass.hasAttr(JPMeta.Attr.JPPACKAGE) || access.getFilter() != null) {
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
    if (checkExists || jpClass.hasAttr(JPMeta.Attr.JPPACKAGE) || access.getFilter() != null) {
      return getObject(id, jpClass, access, auth)
          .map(object -> securityManager.checkDelete(object.getJpPackage(), auth.getRoles()))
          .defaultIfEmpty(false);
    }
    return Mono.just(true);
  }

  private Mono<Boolean> checkUpdate(JPId id, JPMutableData updateData, boolean checkExists, AuthInfo auth) {
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
    Filter accessFilter = access.getFilter();

    Mono<Boolean> result = Mono.just(true);
    // проверки на значение
    if (updateData != null && accessFilter != null) {
      result = result.map(x -> x && checkData(accessFilter, updateData, auth, true));
    }
    // доступ к объекту
    if (checkExists || jpClass.hasAttr(JPMeta.Attr.JPPACKAGE) || accessFilter != null) {
      result = result.flatMap(x -> !x ? Mono.just(Boolean.FALSE) : getObject(id, jpClass, access, auth)
          .map(object -> securityManager.checkUpdate(object.getJpPackage(), auth.getRoles()))
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