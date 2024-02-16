package mp.jprime.dataaccess;

import mp.jprime.dataaccess.params.JPAggregate;
import mp.jprime.dataaccess.params.JPSelect;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.exceptions.JPSelectRightException;
import mp.jprime.security.services.JPResourceAccess;
import mp.jprime.security.services.JPResourceAccessService;

/**
 * Проверка чтения
 */
public interface JPSelectValidator {
  /**
   * Возвращает JPResourceAccessService
   *
   * @return JPResourceAccessService
   */
  JPResourceAccessService getJPResourceAccessService();

  default JPResourceAccess validate(JPSelect query) {
    String classCode = query.getJpClass();
    AuthInfo authInfo = query.getAuth();

    JPResourceAccess access = query.getSource() == Source.USER ? getJPResourceAccessService().checkRead(classCode, authInfo) : null;
    if (access != null && !access.isAccess()) {
      throw new JPSelectRightException(classCode);
    }
    return access;
  }

  default JPResourceAccess validate(JPAggregate query) {
    String classCode = query.getJpClass();
    AuthInfo authInfo = query.getAuth();

    JPResourceAccess access = query.getSource() == Source.USER ? getJPResourceAccessService().checkRead(classCode, authInfo) : null;
    if (access != null && !access.isAccess()) {
      throw new JPSelectRightException(classCode);
    }
    return access;
  }
}
