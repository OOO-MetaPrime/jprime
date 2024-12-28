package mp.jprime.security;

/**
 * Настройка уровня доступа
 */
public interface JPSecurityPackageAccess {
  /**
   * Доступ на чтение
   *
   * @return Да/Нет
   */
  boolean isRead();

  /**
   * Доступ на создание
   *
   * @return Да/Нет
   */
  boolean isCreate();

  /**
   * Доступ на изменение
   *
   * @return Да/Нет
   */
  boolean isUpdate();

  /**
   * Доступ на удаление
   *
   * @return Да/Нет
   */
  boolean isDelete();

  /**
   * Роль
   *
   * @return Роль
   */
  String getRole();
}
