package mp.jprime.dataaccess;

import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.dataaccess.params.JPDelete;
import mp.jprime.dataaccess.params.JPUpdate;
import mp.jprime.exceptions.JPIdNotSpecifiedException;
import mp.jprime.meta.JPClass;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.exceptions.JPCreateRightException;
import mp.jprime.security.exceptions.JPUpdateRightException;
import reactor.core.publisher.Mono;

/**
 * Проверка реактивных операций создания, изменения, удаления
 */
public interface JPReactiveCUDValidator extends JPAttrValidator {
  /**
   * Возвращает JPReactiveObjectAccessService
   *
   * @return JPReactiveObjectAccessService
   */
  JPReactiveObjectAccessService getJPReactiveObjectAccessService();

  default Mono<Void> validate(JPClass jpClass, JPCreate query) {
    String classCode = query.getJpClass();
    JPMutableData data = query.getData();
    AuthInfo authInfo = query.getAuth();

    // проверяем доступ на создание
    if (query.getSource() == Source.USER && authInfo != null) {
      return getJPReactiveObjectAccessService().checkCreate(classCode, data, authInfo)
          .map(result -> {
                if (!result) {
                  throw new JPCreateRightException(classCode);
                }
                // убираем значения атрибутов, к которым нет доступа
                validateAttr(jpClass, query);
                return result;
              }
          )
          .then();
    } else {
      return Mono.empty();
    }
  }

  default Mono<Void> validate(JPClass jpClass, JPUpdate query) {
    JPId jpId = query.getJpId();
    if (jpId == null || jpId.getId() == null || jpId.getJpClass() == null) {
      throw new JPIdNotSpecifiedException();
    }
    String classCode = query.getJpClass();
    JPMutableData data = query.getData();
    AuthInfo authInfo = query.getAuth();

    // проверяем доступ на создание
    if (query.getSource() == Source.USER && authInfo != null) {
      return getJPReactiveObjectAccessService().checkUpdateExists(query.getJpId(), data, authInfo)
          .map(result -> {
                if (!result) {
                  throw new JPUpdateRightException(classCode);
                }
                // убираем значения атрибутов, к которым нет доступа
                validateAttr(jpClass, query);
                return result;
              }
          )
          .then();
    } else {
      return Mono.empty();
    }
  }

  default Mono<Void> validate(JPDelete query) {
    JPId jpId = query.getJpId();
    if (jpId == null || jpId.getId() == null || jpId.getJpClass() == null) {
      throw new JPIdNotSpecifiedException();
    }
    String classCode = query.getJpClass();
    AuthInfo authInfo = query.getAuth();

    // проверяем доступ на создание
    if (query.getSource() == Source.USER && authInfo != null) {
      return getJPReactiveObjectAccessService().checkDeleteExists(query.getJpId(), authInfo)
          .map(result -> {
                if (!result) {
                  throw new JPUpdateRightException(classCode);
                }
                return result;
              }
          )
          .then();
    } else {
      return Mono.empty();
    }
  }
}
