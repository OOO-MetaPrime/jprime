package mp.jprime.security.abac.annotations;

import mp.jprime.common.annotations.JPCond;
import mp.jprime.common.annotations.JPTime;
import mp.jprime.security.beans.JPAccessType;

/**
 * Правило для пользователя
 */
public @interface JPEnvironmentRule {
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
   * IP
   *
   * @return IP
   */
  JPCond ip() default @JPCond();

  /**
   * Время действия
   *
   * @return Время действия
   */
  JPTime time() default @JPTime();

  /**
   * Возвращает тип доступа (разрешительный/запретительный)
   *
   * @return Разрешение/Запрет
   */
  JPAccessType effect() default JPAccessType.PERMIT;
}
