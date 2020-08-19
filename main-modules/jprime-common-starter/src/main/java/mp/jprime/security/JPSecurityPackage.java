package mp.jprime.security;

import java.util.Collection;

/**
 * Настройки доступа
 */
public interface JPSecurityPackage {
  /**
   * Код пакета
   *
   * @return Код пакета
   */
  String getCode();

  /**
   * Описание пакета
   *
   * @return Описание пакета
   */
  String getDescription();

  /**
   * Название пакета
   *
   * @return Название пакета
   */
  String getName();

  /**
   * QName пакета
   *
   * @return qName пакета
   */
  String getQName();

  /**
   * Разрешительные настройки
   *
   * @return Разрешительные настройки
   */
  Collection<JPSecurityPackageAccess> getPermitAccess();

  /**
   * Запретительные настройки
   *
   * @return Запретительные настройки
   */
  Collection<JPSecurityPackageAccess> getProhibitionAccess();

  /**
   * Проверка доступа на чтение
   *
   * @param roles Роли
   * @return Да/Нет
   */
  boolean checkRead(Collection<String> roles);

  /**
   * Проверка доступа на удаление
   *
   * @param roles Роли
   * @return Да/Нет
   */
  boolean checkDelete(Collection<String> roles);

  /**
   * Проверка доступа на обновление
   *
   * @param roles Роли
   * @return Да/Нет
   */
  boolean checkUpdate(Collection<String> roles);

  /**
   * Проверка доступа на создание
   *
   * @param roles Роли
   * @return Да/Нет
   */
  boolean checkCreate(Collection<String> roles);

  /**
   * Признак неизменяемой настройки
   *
   * @return Да/Нет
   */
  default boolean isImmutable() {
    return Boolean.TRUE;
  }
}
