package mp.jprime.dataaccess.services;

import mp.jprime.dataaccess.*;
import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.beans.JPObjectAccess;
import mp.jprime.dataaccess.defvalues.JPObjectDefValueParamsBean;
import mp.jprime.dataaccess.defvalues.JPObjectDefValueService;
import mp.jprime.dataaccess.defvalues.JPObjectDefValueServiceAware;
import mp.jprime.dataaccess.params.query.Filter;
import mp.jprime.lang.JPMap;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.JPMeta;
import mp.jprime.meta.beans.JPType;
import mp.jprime.meta.services.JPMetaStorage;
import mp.jprime.parsers.ParserService;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.services.JPResourceAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Реализация проверки доступа к объекту
 */
@Service
public final class JPObjectAccessCommonService extends JPObjectAccessBaseService implements JPObjectAccessService, JPObjectRepositoryServiceAware, JPObjectDefValueServiceAware {
  // Интерфейс создания/изменения объекта
  private JPObjectRepositoryService repo;
  // Хранилище метаинформации
  private JPMetaStorage metaStorage;
  // Парсер типов
  private ParserService parserService;
  // Логика вычисления значений по умолчанию
  private JPObjectDefValueService defValueService;

  @Autowired
  private void setMetaStorage(JPMetaStorage metaStorage) {
    this.metaStorage = metaStorage;
  }

  @Autowired
  private void setParserService(ParserService parserService) {
    this.parserService = parserService;
  }

  @Override
  public void setJpObjectRepositoryService(JPObjectRepositoryService repo) {
    this.repo = repo;
  }

  @Override
  public void setJPObjectDefValueService(JPObjectDefValueService defValueService) {
    this.defValueService = defValueService;
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
    if (jpAttr == null || jpAttr.getRefJpClass() == null) {
      return Boolean.FALSE;
    }
    JPMutableData data = defValueService
        .getDefValues(
            classCode,
            JPObjectDefValueParamsBean.newBuilder()
                .rootId(value)
                .rootJpClassCode(jpAttr.getRefJpClass())
                .refAttrCode(refAttrCode)
                .auth(auth)
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
  public boolean checkCreate(String classCode, JPMap createData, AuthInfo auth) {
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
  public boolean checkUpdate(JPId id, JPMap updateData, AuthInfo auth) {
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
  public boolean checkUpdateExists(JPId id, JPMap updateData, AuthInfo auth) {
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

  private boolean checkUpdate(JPId id, JPMap updateData, boolean checkExists, AuthInfo auth) {
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
    if (updateData != null && accessFilter != null && !checkData(accessFilter, updateData, auth, true)) {
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

  private Collection<JPObject> getObjects(JPClass jpClass, Collection<? extends Comparable> keys, JPResourceAccess access, AuthInfo auth) {
    return repo.getList(
        toSelect(keys, jpClass, access, auth)
    );
  }

  /**
   * Массовая проверка объектов на доступ
   *
   * @param jpClass Класс объектов
   * @param keys    Список id
   * @param auth    AuthInfo
   * @return Список доступов к объектам
   */
  @Override
  public Collection<JPObjectAccess> objectsAccess(JPClass jpClass, Collection<? extends Comparable> keys, AuthInfo auth) {
    keys = keys.stream()
        .map(id -> cast(jpClass, id))
        .collect(Collectors.toList());
    boolean create = checkCreate(jpClass.getCode(), auth);
    Collection<? extends Comparable> read = fillReadAccess(jpClass, keys, auth);
    Collection<? extends Comparable> update = fillUpdateAccess(jpClass, keys, auth);
    Collection<? extends Comparable> delete = fillDeleteAccess(jpClass, keys, auth);

    return keys.stream()
        .map(
            id -> JPObjectAccess.newBuilder()
                .id(id)
                .jpClass(jpClass.getCode())
                .create(create)
                .read(read.contains(id))
                .update(update.contains(id))
                .delete(delete.contains(id))
                .build()
        )
        .collect(Collectors.toList());
  }

  /**
   * Массовая проверка объектов на доступ
   *
   * @param jpClass Класс объектов
   * @param keys    Список id
   * @param auth    AuthInfo
   * @return Список доступов к объектам
   */
  @Override
  public Collection<JPObjectAccess> objectsChangeAccess(JPClass jpClass, Collection<? extends Comparable> keys, AuthInfo auth) {
    keys = keys.stream()
        .map(id -> cast(jpClass, id))
        .collect(Collectors.toList());
    Collection<? extends Comparable> update = fillUpdateAccess(jpClass, keys, auth);
    Collection<? extends Comparable> delete = fillDeleteAccess(jpClass, keys, auth);

    return keys.stream()
        .map(id -> JPObjectAccess.newBuilder()
            .id(id)
            .jpClass(jpClass.getCode())
            .update(update.contains(id))
            .delete(delete.contains(id))
            .build()
        )
        .collect(Collectors.toList());
  }

  private Comparable cast(JPClass jpClass, Comparable id) {
    JPAttr pkAttr = jpClass != null ? jpClass.getPrimaryKeyAttr() : null;
    if (pkAttr == null) {
      return id;
    }
    JPType valueType = pkAttr.getValueType();
    Class valueClass = valueType != null ? valueType.getJavaClass() : null;
    if (valueClass == null) {
      return id;
    }
    return (Comparable) parserService.parseTo(valueClass, id);
  }

  private Collection<? extends Comparable> fillUpdateAccess(JPClass jpClass, Collection<? extends Comparable> keys, AuthInfo auth) {
    if (auth == null || jpClass == null) {
      return Collections.emptyList();
    }
    JPResourceAccess access = accessService.checkUpdate(jpClass.getCode(), auth);
    if (!access.isAccess()) {
      return Collections.emptyList();
    }
    Filter accessFilter = access.getFilter();
    // доступ к объекту
    if (jpClass.hasAttr(JPMeta.Attr.JPPACKAGE) || accessFilter != null) {
      return getObjects(jpClass, keys, access, auth)
          .stream()
          .filter(object -> securityManager.checkUpdate(object.getJpPackage(), auth.getRoles()))
          .map(object -> object.getJpId().getId())
          .collect(Collectors.toList());
    }
    return new ArrayList<>(keys);
  }

  private Collection<Comparable> fillDeleteAccess(JPClass jpClass, Collection<? extends Comparable> keys, AuthInfo auth) {
    if (auth == null || jpClass == null) {
      return Collections.emptyList();
    }
    JPResourceAccess access = accessService.checkDelete(jpClass.getCode(), auth);
    if (!access.isAccess()) {
      return Collections.emptyList();
    }
    // доступ к объекту
    if (jpClass.hasAttr(JPMeta.Attr.JPPACKAGE) || access.getFilter() != null) {
      return getObjects(jpClass, keys, access, auth)
          .stream()
          .filter(object -> securityManager.checkDelete(object.getJpPackage(), auth.getRoles()))
          .map(object -> object.getJpId().getId())
          .collect(Collectors.toList());
    }
    return new ArrayList<>(keys);
  }

  private Collection<Comparable> fillReadAccess(JPClass jpClass, Collection<? extends Comparable> keys, AuthInfo auth) {
    if (auth == null || jpClass == null) {
      return Collections.emptyList();
    }
    JPResourceAccess access = accessService.checkRead(jpClass.getCode(), auth);
    if (!access.isAccess()) {
      return Collections.emptyList();
    }
    // доступ к объекту
    if (jpClass.hasAttr(JPMeta.Attr.JPPACKAGE) || access.getFilter() != null) {
      return getObjects(jpClass, keys, access, auth)
          .stream()
          .filter(object -> securityManager.checkRead(object.getJpPackage(), auth.getRoles()))
          .map(object -> object.getJpId().getId())
          .collect(Collectors.toList());
    }
    return new ArrayList<>(keys);
  }
}
