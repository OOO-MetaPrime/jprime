package mp.jprime.security.abac.services;

import mp.jprime.dataaccess.JPAction;
import mp.jprime.security.abac.Policy;
import mp.jprime.security.abac.PolicySet;

import java.util.Collection;

/**
 * Описание настроек ABAC
 */
public interface JPAbacStorage {
  /**
   * Возвращает загруженные настройки ABAC
   *
   * @return Настройки ABAC
   */
  Collection<PolicySet> getSettings();

  /**
   * Возвращает загруженные настройки ABAC для указанного класса по указанному действию
   *
   * @param jpClass Кодовое имя класса
   * @param action  Действие
   * @return Настройки ABAC для указанного класса
   */
  Collection<Policy> getSettings(String jpClass, JPAction action);

  /**
   * Возвращает список кодов ABAC
   *
   * @return список кодов ABAC
   */
  Collection<String> getSettingsCodes();

  /**
   * Признак наличия ABAC настроек для указанного класса
   *
   * @param jpClass Кодовое имя класса
   * @return Да/Нет
   */
  boolean hasSettings(String jpClass);
}
