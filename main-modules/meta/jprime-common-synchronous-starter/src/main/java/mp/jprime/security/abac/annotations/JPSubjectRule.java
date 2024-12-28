package mp.jprime.security.abac.annotations;

import mp.jprime.common.annotations.JPCond;
import mp.jprime.security.beans.JPAccessType;

/**
 * Правило для пользователя
 */
public @interface JPSubjectRule {
  /**
   * Название правила
   *
   * @return Название правила
   */
  String name();

  /**
   * QName правила
   *
   * @return qName правила
   */
  String qName() default "";

  /**
   * Логин
   *
   * @return Логин
   */
  JPCond username() default @JPCond();

  /**
   * Роли
   *
   * @return Роли
   */
  JPCond role() default @JPCond();

  /**
   * Идентификатор организации
   *
   * @return Идентификатор организации
   */
  JPCond orgId() default @JPCond();

  /**
   * Идентификатор подразделения
   *
   * @return Идентификатор подразделения
   */
  JPCond depId() default @JPCond();

  /**
   * Возвращает тип доступа (разрешительный/запретительный)
   *
   * @return Разрешение/Запрет
   */
  JPAccessType effect() default JPAccessType.PERMIT;
}
