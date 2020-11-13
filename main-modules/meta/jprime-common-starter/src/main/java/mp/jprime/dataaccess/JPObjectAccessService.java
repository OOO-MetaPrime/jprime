package mp.jprime.dataaccess;

import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.security.AuthInfo;
import mp.jprime.security.services.JPResourceAccess;

/**
 * Интерфейс проверки доступа к объекту
 */
public interface JPObjectAccessService {
  /**
   * Проверка доступа на создание
   *
   * @param classCode Код метаописания
   * @param auth      AuthInfo
   * @return Да/Нет
   */
  boolean checkCreate(String classCode, AuthInfo auth);

  /**
   * Проверка доступа на чтение
   *
   * @param id   Идентификатор объекта
   * @param auth AuthInfo
   * @return Да/Нет
   */
  boolean checkRead(JPId id, AuthInfo auth);

  /**
   * Проверка доступа на удаление
   *
   * @param id   Идентификатор объекта
   * @param auth AuthInfo
   * @return Да/Нет
   */
  boolean checkDelete(JPId id, AuthInfo auth);

  /**
   * Проверка доступа на обновление
   *
   * @param id   Идентификатор объекта
   * @param auth AuthInfo
   * @return Да/Нет
   */
  boolean checkUpdate(JPId id, AuthInfo auth);
}
