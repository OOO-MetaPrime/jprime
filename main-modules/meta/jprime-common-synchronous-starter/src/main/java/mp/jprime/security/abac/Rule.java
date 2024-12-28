package mp.jprime.security.abac;

import mp.jprime.security.beans.JPAccessType;

/**
 * Правило
 */
public interface Rule {
  /**
   * Название правила
   *
   * @return Название правила
   */
  String getName();

  /**
   * QName правила
   *
   * @return qName правила
   */
  String getQName();

  /**
   * Возвращает тип доступа (разрешительный/запретительный)
   *
   * @return Разрешение/Запрет
   */
  JPAccessType getEffect();
}
