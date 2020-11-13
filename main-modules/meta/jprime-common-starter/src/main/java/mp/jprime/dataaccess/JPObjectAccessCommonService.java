package mp.jprime.dataaccess;

import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.params.JPSelect;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPMeta;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.services.JPResourceAccessService;
import mp.jprime.security.services.JPResourceAccess;
import mp.jprime.security.services.JPSecurityStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Реализация проверки доступа к объекту
 */
@Service
public class JPObjectAccessCommonService implements JPObjectAccessService {
  // Проверка доступа
  private JPResourceAccessService accessService;
  // Хранилище настроек RBAC
  private JPSecurityStorage securityManager;
  // Интерфейс создания/изменения объекта
  private JPObjectRepositoryService repo;
  // Хранилище метаинформации
  private JPMetaStorage metaStorage;

  @Autowired
  private void setAccessService(JPResourceAccessService accessService) {
    this.accessService = accessService;
  }

  @Autowired
  private void setRepo(JPObjectRepositoryService repo) {
    this.repo = repo;
  }

  @Autowired
  private void setMetaStorage(JPMetaStorage metaStorage) {
    this.metaStorage = metaStorage;
  }

  @Autowired
  private void setSecurityManager(JPSecurityStorage securityManager) {
    this.securityManager = securityManager;
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
    if (classCode == null || auth == null) {
      return false;
    }
    JPResourceAccess access = accessService.checkCreate(classCode, auth);
    return access == null || access.isAccess();
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
    JPObject object = getObject(id, jpClass, access, auth);
    if (object == null || !securityManager.checkRead(object.getJpPackage(), auth.getRoles())) {
      return false;
    }
    return true;
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
    JPObject object = getObject(id, jpClass, access, auth);
    if (object == null || !securityManager.checkDelete(object.getJpPackage(), auth.getRoles())) {
      return false;
    }
    return true;
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
    JPObject object = getObject(id, jpClass, access, auth);
    if (object == null || !securityManager.checkUpdate(object.getJpPackage(), auth.getRoles())) {
      return false;
    }
    return true;
  }

  private JPObject getObject(JPId id, JPClass jpClass, JPResourceAccess access, AuthInfo auth) {
    return repo.getObject(
        JPSelect.from(id.getJpClass())
            .attr(jpClass.hasAttr(JPMeta.Attr.JPPACKAGE) ? JPMeta.Attr.JPPACKAGE : jpClass.getPrimaryKeyAttr().getCode())
            .where(
                getFilter(
                    Filter.attr(jpClass.getPrimaryKeyAttr()).eq(id.getId()),
                    access
                )
            )
            .auth(auth)
            .build()
    );
  }

  private Filter getFilter(Filter selectWhere, JPResourceAccess access) {
    Filter filter = access == null ? null : access.getFilter();
    if (filter == null) {
      filter = selectWhere;
    } else if (selectWhere != null) {
      filter = Filter.and(filter, selectWhere);
    }
    return filter;
  }
}
