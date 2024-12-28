package mp.jprime.security.abac;

import java.util.Collection;

/**
 * Группа политик
 */
public interface PolicySet {
  /**
   * Код политики
   *
   * @return Код политики
   */
  String getCode();

  /**
   * Название группы
   *
   * @return Название группы
   */
  String getName();

  /**
   * QName группы
   *
   * @return qName группы
   */
  String getQName();

  /**
   * Список классов для применения политики
   *
   * @return Список классов
   */
  PolicyTarget getTarget();

  /**
   * Политики
   *
   * @return Политики
   */
  Collection<Policy> getPolicies();

  /**
   * Признак неизменяемой настройки
   *
   * @return Да/Нет
   */
  default boolean isImmutable() {
    return Boolean.TRUE;
  }
}
