package mp.jprime.security.services;

import mp.jprime.security.JPSecurityPackage;

import java.util.Collection;

/**
 * Описание настроек безопаности
 */
public interface JPSecurityStorage {
  /**
   * Возвращает загруженные настройки доступа
   *
   * @return Настройки доступа
   */
  Collection<JPSecurityPackage> getPackages();

  /**
   * Возвращает настройку доступа по коду
   *
   * @param code Код
   * @return Настройка доступа
   */
  JPSecurityPackage getJPPackageByCode(String code);

  /**
   * Проверка доступа на чтение
   *
   * @param packageCode Код пакета
   * @param roles       Роли
   * @return Да/Нет
   */
  boolean checkRead(String packageCode, Collection<String> roles);

  /**
   * Проверка доступа на удаление
   *
   * @param packageCode Код пакета
   * @param roles       Роли
   * @return Да/Нет
   */
  boolean checkDelete(String packageCode, Collection<String> roles);

  /**
   * Проверка доступа на обновление
   *
   * @param packageCode Код пакета
   * @param roles       Роли
   * @return Да/Нет
   */
  boolean checkUpdate(String packageCode, Collection<String> roles);

  /**
   * Проверка доступа на создание
   *
   * @param packageCode Код пакета
   * @param roles       Роли
   * @return Да/Нет
   */
  boolean checkCreate(String packageCode, Collection<String> roles);
}
