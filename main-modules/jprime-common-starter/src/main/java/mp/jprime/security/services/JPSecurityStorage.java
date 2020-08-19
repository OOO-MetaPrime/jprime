package mp.jprime.security.services;

import mp.jprime.security.JPSecurityPackage;

import java.util.Collection;

/**
 * Описание настроек RBAC
 */
public interface JPSecurityStorage {
  /**
   * Возвращает загруженные настройки RBAC
   *
   * @return Настройки RBAC
   */
  Collection<JPSecurityPackage> getPackages();

  /**
   * Возвращает настройку RBAC по коду
   *
   * @param code Код
   * @return Настройка RBAC
   */
  JPSecurityPackage getJPPackageByCode(String code);

  /**
   * Проверка всех пакетов на доступ на чтение
   *
   * @param roles Роли
   * @return Список кодов пакета
   */
  Collection<String> checkRead(Collection<String> roles);

  /**
   * Проверка RBAC на чтение
   *
   * @param packageCode Код пакета
   * @param roles       Роли
   * @return Да/Нет
   */
  boolean checkRead(String packageCode, Collection<String> roles);

  /**
   * Проверка RBAC на удаление
   *
   * @param packageCode Код пакета
   * @param roles       Роли
   * @return Да/Нет
   */
  boolean checkDelete(String packageCode, Collection<String> roles);

  /**
   * Проверка RBAC на обновление
   *
   * @param packageCode Код пакета
   * @param roles       Роли
   * @return Да/Нет
   */
  boolean checkUpdate(String packageCode, Collection<String> roles);

  /**
   * Проверка RBAC на создание
   *
   * @param packageCode Код пакета
   * @param roles       Роли
   * @return Да/Нет
   */
  boolean checkCreate(String packageCode, Collection<String> roles);
}
