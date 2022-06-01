package mp.jprime.dataaccess;

import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.dataaccess.beans.JPObjectAccess;
import mp.jprime.meta.JPClass;
import mp.jprime.security.AuthInfo;

import java.util.Collection;

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
   * Проверка доступа на создание из другого объекта
   *
   * @param classCode   Код метаописания
   * @param refAttrCode Ссылочный атрибут
   * @param value       Значение ссылочного атрибута
   * @param auth        AuthInfo
   * @return Да/Нет
   */
  boolean checkCreate(String classCode, String refAttrCode, Comparable value, AuthInfo auth);

  /**
   * Проверка доступа на создание
   *
   * @param classCode  Код метаописания
   * @param createData Данные для создания
   * @param auth       AuthInfo
   * @return Да/Нет
   */
  boolean checkCreate(String classCode, JPMutableData createData, AuthInfo auth);

  /**
   * Проверка доступа на чтение
   *
   * @param classCode Код метаописания
   * @param auth      AuthInfo
   * @return Да/Нет
   */
  boolean checkRead(String classCode, AuthInfo auth);

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
   * @param classCode Код метаописания
   * @param auth      AuthInfo
   * @return Да/Нет
   */
  boolean checkDelete(String classCode, AuthInfo auth);

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
   * @param classCode Код метаописания
   * @param auth      AuthInfo
   * @return Да/Нет
   */
  boolean checkUpdate(String classCode, AuthInfo auth);

  /**
   * Проверка доступа на обновление
   *
   * @param id   Идентификатор объекта
   * @param auth AuthInfo
   * @return Да/Нет
   */
  boolean checkUpdate(JPId id, AuthInfo auth);

  /**
   * Проверка доступа на обновление
   *
   * @param id         Идентификатор объекта
   * @param updateData Данные для обновления
   * @param auth       AuthInfo
   * @return Да/Нет
   */
  boolean checkUpdate(JPId id, JPMutableData updateData, AuthInfo auth);

  /**
   * Проверка доступа на чтение
   *
   * @param id   Идентификатор объекта
   * @param auth AuthInfo
   * @return Да/Нет
   */
  boolean checkReadExists(JPId id, AuthInfo auth);

  /**
   * Проверка доступа на удаление
   *
   * @param id   Идентификатор объекта + наличие объекта
   * @param auth AuthInfo
   * @return Да/Нет
   */
  boolean checkDeleteExists(JPId id, AuthInfo auth);

  /**
   * Проверка доступа на обновление
   *
   * @param id   Идентификатор объекта + наличие объекта
   * @param auth AuthInfo
   * @return Да/Нет
   */
  boolean checkUpdateExists(JPId id, AuthInfo auth);

  /**
   * Проверка доступа на обновление
   *
   * @param id         Идентификатор объекта + наличие объекта
   * @param updateData Данные для обновления
   * @param auth       AuthInfo
   * @return Да/Нет
   */
  boolean checkUpdateExists(JPId id, JPMutableData updateData, AuthInfo auth);

  /**
   * Массовая проверка объектов на доступ
   *
   * @param jpClass Класс объектов
   * @param keys    Список id
   * @param auth    AuthInfo
   * @return Список доступов к объектам
   */
  Collection<JPObjectAccess> objectsAccess(JPClass jpClass, Collection<? extends Comparable> keys, AuthInfo auth);

  /**
   * Массовая проверка объектов на изменение
   *
   * @param jpClass Класс объектов
   * @param keys    Список id
   * @param auth    AuthInfo
   * @return Список доступов к объектам
   */
  Collection<JPObjectAccess> objectsChangeAccess(JPClass jpClass, Collection<? extends Comparable> keys, AuthInfo auth);
}
