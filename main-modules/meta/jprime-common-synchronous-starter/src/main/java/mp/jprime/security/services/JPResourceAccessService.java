package mp.jprime.security.services;

import mp.jprime.security.AuthInfo;


/**
 * Проверка доступа к объектам
 */
public interface JPResourceAccessService {
  /**
   * Проверка доступа на чтение
   *
   * @param classCode Код метаописания
   * @param auth      AuthInfo
   * @return Да/Нет
   */
  JPResourceAccess checkRead(String classCode, AuthInfo auth);

  /**
   * Проверка доступа на удаление
   *
   * @param classCode Код метаописания
   * @param auth      AuthInfo
   * @return Да/Нет
   */
  JPResourceAccess checkDelete(String classCode, AuthInfo auth);

  /**
   * Проверка доступа на обновление
   *
   * @param classCode Код метаописания
   * @param auth      AuthInfo
   * @return Да/Нет
   */
  JPResourceAccess checkUpdate(String classCode, AuthInfo auth);

  /**
   * Проверка доступа на создание
   *
   * @param classCode Код метаописания
   * @param auth      AuthInfo
   * @return Да/Нет
   */
  JPResourceAccess checkCreate(String classCode, AuthInfo auth);
}
