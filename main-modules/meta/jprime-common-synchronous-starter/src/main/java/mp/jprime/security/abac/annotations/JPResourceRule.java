package mp.jprime.security.abac.annotations;

import mp.jprime.common.annotations.JPCond;
import mp.jprime.security.beans.JPAccessType;

/**
 * Правило для объекта (ресурса)
 */
public @interface JPResourceRule {
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
   * Атрибут
   *
   * @return Атрибут
   */
  String attr();

  /**
   * Условие
   *
   * @return Условие
   */
  JPCond cond();

  /**
   * Возвращает тип доступа (разрешительный/запретительный)
   *
   * @return Разрешение/Запрет
   */
  JPAccessType effect() default JPAccessType.PERMIT;
}
