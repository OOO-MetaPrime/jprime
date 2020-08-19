package mp.jprime.security.abac;

import mp.jprime.dataaccess.JPAction;

import java.util.Collection;

/**
 * Политика
 */
public interface Policy {
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
   * Действия
   *
   * @return Действия
   */
  Collection<JPAction> getActions();

  /**
   * Возвращает правила для пользователя
   *
   * @return правила для пользователя
   */
  Collection<SubjectRule> getSubjectRules();

  /**
   * Возвращает правила для объекта
   *
   * @return правила для объекта
   */
  Collection<ResourceRule> getResourceRules();

  /**
   * Возвращает правила для окружения
   *
   * @return правила для окружения
   */
  Collection<EnviromentRule> getEnviromentRules();
}
