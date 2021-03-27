package mp.jprime.dataaccess.services;

import mp.jprime.dataaccess.JPObjectAccessService;
import mp.jprime.dataaccess.JPObjectRepositoryService;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPMeta;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.services.JPResourceAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Реализация проверки доступа к объекту
 */
@Service
public class JPObjectAccessCommonService extends JPObjectAccessBaseService implements JPObjectAccessService {
  // Интерфейс создания/изменения объекта
  private JPObjectRepositoryService repo;

  @Autowired
  private void setRepo(JPObjectRepositoryService repo) {
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
  public boolean checkCreate(String classCode, AuthInfo auth) {
    return isCreateCheck(classCode, auth);
  }

  /**
   * Проверка доступа на чтение
   *
   * @param id   Идентификатор объекта
   * @param auth AuthInfo
   * @return Да/Нет
   */
  @Override
  public boolean checkRead(JPId id, AuthInfo auth) {
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
  public boolean checkDelete(JPId id, AuthInfo auth) {
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
  public boolean checkUpdate(JPId id, AuthInfo auth) {
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
  public boolean checkReadExists(JPId id, AuthInfo auth) {
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
  public boolean checkDeleteExists(JPId id, AuthInfo auth) {
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
  public boolean checkUpdateExists(JPId id, AuthInfo auth) {
    return checkUpdate(id, Boolean.TRUE, auth);
  }


  private boolean checkRead(JPId id, boolean checkExists, AuthInfo auth) {
    if (id == null) {
      return false;
    }
    JPClass jpClass = metaStorage.getJPClassByCode(id.getJpClass());
    if (jpClass == null) {
      return false;
    }
    JPResourceAccess access = accessService.checkRead(id.getJpClass(), auth);
    if (!access.isAccess()) {
      return false;
    }
    // доступ к объекту
    if (checkExists || jpClass.hasAttr(JPMeta.Attr.JPPACKAGE)) {
      JPObject object = getObject(id, jpClass, access, auth);
      if (object == null || !securityManager.checkRead(object.getJpPackage(), auth.getRoles())) {
        return false;
      }
    }
    return true;
  }

  private boolean checkDelete(JPId id, boolean checkExists, AuthInfo auth) {
    if (id == null || auth == null) {
      return false;
    }
    JPClass jpClass = metaStorage.getJPClassByCode(id.getJpClass());
    if (jpClass == null) {
      return false;
    }
    JPResourceAccess access = accessService.checkDelete(id.getJpClass(), auth);
    if (!access.isAccess()) {
      return false;
    }
    // доступ к объекту
    if (checkExists || jpClass.hasAttr(JPMeta.Attr.JPPACKAGE)) {
      JPObject object = getObject(id, jpClass, access, auth);
      if (object == null || !securityManager.checkDelete(object.getJpPackage(), auth.getRoles())) {
        return false;
      }
    }
    return true;
  }

  private boolean checkUpdate(JPId id, boolean checkExists, AuthInfo auth) {
    if (id == null || auth == null) {
      return false;
    }
    JPClass jpClass = metaStorage.getJPClassByCode(id.getJpClass());
    if (jpClass == null) {
      return false;
    }
    JPResourceAccess access = accessService.checkUpdate(id.getJpClass(), auth);
    if (!access.isAccess()) {
      return false;
    }
    // доступ к объекту
    if (checkExists || jpClass.hasAttr(JPMeta.Attr.JPPACKAGE)) {
      JPObject object = getObject(id, jpClass, access, auth);
      if (object == null || !securityManager.checkUpdate(object.getJpPackage(), auth.getRoles())) {
        return false;
      }
    }
    return true;
  }

  private JPObject getObject(JPId id, JPClass jpClass, JPResourceAccess access, AuthInfo auth) {
    return repo.getObject(
        toSelect(id, jpClass, access, auth)
    );
  }
}
