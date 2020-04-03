package mp.jprime.security.annotations;

import mp.jprime.security.beans.JPAccessType;

/**
 * Настройка доступа
 */
public @interface JPAccess {

  /**
   * Тип доступа - разрешительный/запретительный
   *
   * @return JPAccessType
   */
  JPAccessType type() default JPAccessType.PERMIT;
  /**
   * Код роли
   *
   * @return Код роли
   */
  String role();
  /**
   * Чтение
   *
   * @return Да/Нет
   */
  boolean read() default false;

  /**
   * Создание
   *
   * @return Да/Нет
   */
  boolean create() default false;

  /**
   * Обновление
   *
   * @return Да/Нет
   */
  boolean update() default false;

  /**
   * Удаление
   *
   * @return Да/Нет
   */
  boolean delete() default false;
}
