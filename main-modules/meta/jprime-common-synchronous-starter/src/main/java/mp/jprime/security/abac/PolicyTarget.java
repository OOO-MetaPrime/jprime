package mp.jprime.security.abac;

import java.util.Collection;

/**
 * Условие - метаописание класса
 */
public interface PolicyTarget {
  /**
   * Возвращает условие на метаописание класса
   *
   * @return Условие на метаописание класса
   */
  Collection<String> getJpClassCodes();
}
