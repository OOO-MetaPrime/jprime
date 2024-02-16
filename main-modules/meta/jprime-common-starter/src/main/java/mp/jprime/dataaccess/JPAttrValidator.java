package mp.jprime.dataaccess;

import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.dataaccess.params.JPBatchUpdate;
import mp.jprime.dataaccess.params.JPCreate;
import mp.jprime.dataaccess.params.JPUpdate;
import mp.jprime.meta.JPAttr;
import mp.jprime.meta.JPClass;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.services.JPSecurityStorage;

import java.util.Map;

/**
 * Проверка доступа к атрибутам при операциях создания, изменения
 */
public interface JPAttrValidator {
  /**
   * Возвращает JPSecurityStorage
   *
   * @return JPSecurityStorage
   */
  JPSecurityStorage getSecurityStorage();

  /**
   * Проверка прав доступа к атрибутам при создании
   *
   * @param jpClass JPClass
   * @param query   Запрос
   */
  default void validateAttr(JPClass jpClass, JPCreate query) {
    JPMutableData data = query.getData();
    AuthInfo authInfo = query.getAuth();

    // убираем значения атрибутов, к которым нет доступа
    data.entrySet().stream()
        .filter(entry -> {
          JPAttr jpAttr = jpClass.getAttr(entry.getKey());
          if (jpAttr == null) {
            return true;
          } else {
            return query.getSource() == Source.USER && authInfo != null &&
                !getSecurityStorage().checkCreate(jpAttr.getJpPackage(), authInfo.getRoles());
          }
        })
        .map(Map.Entry::getKey)
        .toList()
        .forEach(data::remove);
  }

  /**
   * Проверка прав доступа к атрибутам при обновлении
   *
   * @param jpClass JPClass
   * @param query   Запрос
   */
  default void validateAttr(JPClass jpClass, JPUpdate query) {
    JPMutableData data = query.getData();
    AuthInfo authInfo = query.getAuth();

    data.entrySet()
        .stream()
        .filter(entry -> {
          JPAttr jpAttr = jpClass.getAttr(entry.getKey());
          if (jpAttr == null) {
            return true;
          } else {
            return query.getSource() == Source.USER && authInfo != null &&
                !getSecurityStorage().checkUpdate(jpAttr.getJpPackage(), authInfo.getRoles());
          }
        })
        .map(Map.Entry::getKey)
        .toList()
        .forEach(data::remove);
  }

  /**
   * Проверка прав доступа к атрибутам при обновлении
   *
   * @param jpClass JPClass
   * @param query   JPBatchUpdate
   */
  default void validateAttr(JPClass jpClass, JPBatchUpdate query) {
    AuthInfo authInfo = query.getAuth();
    query.forEach((id, data) -> data.entrySet()
        .stream()
        .filter(entry -> {
          JPAttr jpAttr = jpClass.getAttr(entry.getKey());
          if (jpAttr == null) {
            return true;
          } else {
            return query.getSource() == Source.USER && authInfo != null &&
                !getSecurityStorage().checkUpdate(jpAttr.getJpPackage(), authInfo.getRoles());
          }
        })
        .map(Map.Entry::getKey)
        .toList()
        .forEach(data::remove));
  }
}
