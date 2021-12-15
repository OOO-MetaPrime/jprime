package mp.jprime.dataaccess.services;

import mp.jprime.dataaccess.*;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.defvalues.JPObjectDefValueParamsBean;
import mp.jprime.dataaccess.defvalues.JPObjectDefValueService;
import mp.jprime.dataaccess.defvalues.JPObjectDefValueServiceAware;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPMeta;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.services.JPResourceAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Реализация проверки доступа к объекту
 */
@Service
public class JPObjectAccessCommonService extends JPObjectAccessBaseService implements JPObjectAccessService, JPObjectRepositoryServiceAware, JPObjectDefValueServiceAware {
  // Интерфейс создания/изменения объекта
  private JPObjectRepositoryService repo;
  // Хранилище метаинформации
  private JPMetaStorage metaStorage;
  // Логика вычисления значений по умолчанию
  private JPObjectDefValueService defValueService;

  @Override
  public void setJpObjectRepositoryService(JPObjectRepositoryService repo) {
    this.repo = repo;
  }

  @Autowired
  private void setMetaStorage(JPMetaStorage metaStorage) {
    this.metaStorage = metaStorage;
  }

  @Override
  public void setJPObjectDefValuesService(JPObjectDefValueService defValueService) {
    this.defValueService = defValueService;
  }

  /**
   * Указание ссылок
   */
  @Autowired(required = false)
  private void setAwares(Collection<JPObjectAccessServiceAware> awares) {
    for (JPObjectAccessServiceAware aware : awares) {
      aware.setJpObjectAccessService(this);
    }
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
  public boolean checkCreate(String classCode, String refAttrCode, Comparable value, AuthInfo auth) {
    if (refAttrCode == null || value == null) {
      return isCreateCheck(classCode, null, auth);
    }
    JPClass jpClass = metaStorage.getJPClassByCode(classCode);
    JPAttr jpAttr = jpClass != null ? jpClass.getAttr(refAttrCode) : null;
    if (jpAttr == null || jpAttr.getRefJpClassCode() == null) {
      return Boolean.FALSE;
    }
    JPMutableData data = defValueService
        .getDefValues(
            classCode,
            JPObjectDefValueParamsBean.newBuilder()
                .rootId(value)
                .rootJpClassCode(jpAttr.getRefJpClassCode())
                .refAttrCode(refAttrCode)
                .auth(auth)
                .source(Source.USER)
                .build()
        );
    data.putIfAbsent(refAttrCode, value);
    return isCreateCheck(classCode, data, auth);
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
    return isCreateCheck(classCode, null, auth);
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
  public boolean checkCreate(String classCode, JPMutableData createData, AuthInfo auth) {
    return isCreateCheck(classCode, createData, auth);
  }

  /**
   * Проверка доступа на чтение
   *
   * @param classCode Код метаописания
   * @param auth      AuthInfo
   * @return Да/Нет
   */
  @Override
  public boolean checkRead(String classCode, AuthInfo auth) {
    return isReadCheck(classCode, auth);
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
   * @param classCode Код метаописания
   * @param auth      AuthInfo
   * @return Да/Нет
   */
  @Override
  public boolean checkDelete(String classCode, AuthInfo auth) {
    return isDeleteCheck(classCode, auth);
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
   * @param classCode Код метаописания
   * @param auth      AuthInfo
   * @return Да/Нет
   */
  @Override
  public boolean checkUpdate(String classCode, AuthInfo auth) {
    return isUpdateCheck(classCode, auth);
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
  public boolean checkUpdate(JPId id, JPMutableData updateData, AuthInfo auth) {
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
  public boolean checkUpdateExists(JPId id, JPMutableData updateData, AuthInfo auth) {
    return checkUpdate(id, updateData, Boolean.TRUE, auth);
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
    if (checkExists || jpClass.hasAttr(JPMeta.Attr.JPPACKAGE) || access.getFilter() != null) {
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
    if (checkExists || jpClass.hasAttr(JPMeta.Attr.JPPACKAGE) || access.getFilter() != null) {
      JPObject object = getObject(id, jpClass, access, auth);
      if (object == null || !securityManager.checkDelete(object.getJpPackage(), auth.getRoles())) {
        return false;
      }
    }
    return true;
  }

  private boolean checkUpdate(JPId id, JPMutableData updateData, boolean checkExists, AuthInfo auth) {
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
    Filter accessFilter = access.getFilter();
    // проверки на значение
    if (updateData != null && accessFilter != null && !checkData(accessFilter, updateData, auth)) {
      return false;
    }
    // доступ к объекту
    if (checkExists || jpClass.hasAttr(JPMeta.Attr.JPPACKAGE) || accessFilter != null) {
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
