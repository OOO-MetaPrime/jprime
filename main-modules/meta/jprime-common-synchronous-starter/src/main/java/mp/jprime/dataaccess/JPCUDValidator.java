package mp.jprime.dataaccess;

import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.dataaccess.params.*;
import mp.jprime.exceptions.JPDeleteCondNotSpecifiedException;
import mp.jprime.exceptions.JPUpdateCondNotSpecifiedException;
import mp.jprime.exceptions.JPIdNotSpecifiedException;
import mp.jprime.meta.JPClass;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.exceptions.JPCreateRightException;
import mp.jprime.security.exceptions.JPDeleteRightException;
import mp.jprime.security.exceptions.JPUpdateRightException;

/**
 * Проверка операций создания, изменения, удаления
 */
public interface JPCUDValidator extends JPAttrValidator {
  /**
   * Возвращает JPObjectAccessService
   *
   * @return JPObjectAccessService
   */
  JPObjectAccessService getJPObjectAccessService();

  default void validate(JPClass jpClass, JPCreate query) {
    String classCode = query.getJpClass();
    JPMutableData data = query.getData();
    AuthInfo auth = query.getAuth();

    if (query.getSource() == Source.USER
        && auth != null
        && !getJPObjectAccessService().checkCreate(classCode, data, auth)) {
      throw new JPCreateRightException(classCode);
    }

    validateAttr(jpClass, query);
  }

  default void validate(JPBatchCreate query) {
    String classCode = query.getJpClass();
    AuthInfo auth = query.getAuth();

    if (query.getSource() == Source.USER
        && auth != null
        && query.getData().stream()
        .noneMatch(data -> getJPObjectAccessService().checkCreate(classCode, data, auth))
    ) {
      throw new JPCreateRightException(classCode);
    }
  }

  default void validate(JPClass jpClass, JPUpdate query) {
    JPId jpId = query.getJpId();
    if (jpId == null || jpId.getId() == null || jpId.getJpClass() == null) {
      throw new JPIdNotSpecifiedException();
    }
    String classCode = query.getJpClass();

    // проверяем доступ на обновление
    AuthInfo auth = query.getAuth();
    if (query.getSource() == Source.USER
        && auth != null
        && !getJPObjectAccessService().checkUpdateExists(query.getJpId(), query.getData(), auth)) {
      throw new JPUpdateRightException(classCode);
    }
    // Убираем значения атрибутов, к которым нет доступа
    validateAttr(jpClass, query);
  }

  default void validate(JPClass jpClass, JPConditionalUpdate query) {
    String classCode = query.getJpClass();
    if (query.getAuth() != null && query.getSource() == Source.USER
        && !getJPObjectAccessService().checkUpdate(classCode, query.getAuth())) {
      throw new JPUpdateRightException(classCode);
    }
    if (query.getWhere() == null) {
      throw new JPUpdateCondNotSpecifiedException();
    }

    // Убираем значения атрибутов, к которым нет доступа
    validateAttr(jpClass, query);
  }

  default void validate(JPClass jpClass, JPBatchUpdate query) {
    String classCode = query.getJpClass();
    if (classCode == null) {
      throw new JPIdNotSpecifiedException();
    }

    // проверяем доступ на обновление
    AuthInfo auth = query.getAuth();
    if (query.getSource() == Source.USER && auth != null
        && query.getBatches().entrySet()
        .stream()
        .noneMatch(entry -> getJPObjectAccessService().checkUpdate(JPId.get(classCode, entry.getKey()), entry.getValue(), auth))) {
      throw new JPUpdateRightException(classCode);
    }
    // Убираем значения атрибутов, к которым нет доступа
    validateAttr(jpClass, query);
  }

  default void validate(JPDelete query) {
    JPId jpId = query.getJpId();
    if (jpId == null || jpId.getId() == null || jpId.getJpClass() == null) {
      throw new JPIdNotSpecifiedException();
    }
    String classCode = query.getJpClass();

    // проверяем доступ на удаление
    AuthInfo auth = query.getAuth();
    if (query.getSource() == Source.USER
        && auth != null
        && !getJPObjectAccessService().checkDeleteExists(query.getJpId(), auth)) {
      throw new JPDeleteRightException(classCode);
    }
  }

  default void validate(JPConditionalDelete query) {
    String classCode = query.getJpClass();
    if (query.getAuth() != null && query.getSource() == Source.USER
        && !getJPObjectAccessService().checkDelete(classCode, query.getAuth())) {
      throw new JPUpdateRightException(classCode);
    }
    if (query.getWhere() == null) {
      throw new JPDeleteCondNotSpecifiedException();
    }
  }
}
