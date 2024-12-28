package mp.jprime.dataaccess;

import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.dataaccess.params.*;
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
    AuthInfo auth = query.getAuth();

    // убираем значения атрибутов, к которым нет доступа
    data.entrySet().stream()
        .filter(entry -> {
          JPAttr jpAttr = jpClass.getAttr(entry.getKey());
          if (jpAttr == null) {
            return true;
          } else if (query.isSystemAttr(jpAttr.getCode())) {
            return false;
          } else {
            return query.getSource() == Source.USER && auth != null &&
                !getSecurityStorage().checkCreate(jpAttr.getJpPackage(), auth.getRoles());
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
    validateAttrForUpdate(jpClass, query);
  }

  /**
   * Проверка прав доступа к атрибутам при обновлении
   *
   * @param jpClass JPClass
   * @param query   Запрос
   */
  default void validateAttr(JPClass jpClass, JPConditionalUpdate query) {
    validateAttrForUpdate(jpClass, query);
  }

  /**
   * Проверка прав доступа к атрибутам при обновлении
   *
   * @param jpClass JPClass
   * @param query   JPSave
   */
  default void validateAttrForUpdate(JPClass jpClass, JPSave query) {
    JPMutableData data = query.getData();
    AuthInfo auth = query.getAuth();
    Source qSource = query.getSource();

    data.entrySet()
        .stream()
        .filter(entry -> {
          JPAttr jpAttr = jpClass.getAttr(entry.getKey());
          if (jpAttr == null) {
            return true;
          } else if (query.isSystemAttr(jpAttr.getCode())) {
            return false;
          } else {
            return qSource == Source.USER && auth != null &&
                !getSecurityStorage().checkUpdate(jpAttr.getJpPackage(), auth.getRoles());
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
    AuthInfo auth = query.getAuth();
    query.forEach((id, data) -> data.entrySet()
        .stream()
        .filter(entry -> {
          JPAttr jpAttr = jpClass.getAttr(entry.getKey());
          if (jpAttr == null) {
            return true;
          } else {
            return query.getSource() == Source.USER && auth != null &&
                !getSecurityStorage().checkUpdate(jpAttr.getJpPackage(), auth.getRoles());
          }
        })
        .map(Map.Entry::getKey)
        .toList()
        .forEach(data::remove));
  }
}
