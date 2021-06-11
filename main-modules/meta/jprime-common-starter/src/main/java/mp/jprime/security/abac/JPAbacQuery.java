package mp.jprime.security.abac;

import java.util.Collection;

/**
 * Запрос для поиска настроек политик безопасности
 */
public interface JPAbacQuery {
  /**
   * Имя для фильтрации
   *
   * @return Значение для поиска по имени
   */
  String getName();

  /**
   * Список ролей для фильтрации
   *
   * @return Список ролей
   */
  Collection<String> getRoles();

  /**
   * Список классов для фильтрации
   *
   * @return Список классов
   */
  Collection<String> getJpClassCodes();

  /**
   * Поиск по созданию
   *
   * @return Поиск по созданию
   */
  boolean useCreate();

  /**
   * Поиск по чтению
   *
   * @return Поиск по чтению
   */
  boolean useRead();

  /**
   * Поиск по изменению
   *
   * @return Поиск по изменению
   */
  boolean useUpdate();

  /**
   * Поиск по удалению
   *
   * @return Поиск по удалению
   */
  boolean useDelete();

  /**
   * Поиск по настройкам на разрешение
   *
   * @return Поиск по настройкам на разрешение
   */
  boolean usePermit();

  /**
   * Поиск по настройкам на запрет
   *
   * @return Поиск по настройкам на запрет
   */
  boolean useProhibition();

  /**
   * Поиск по наличию правил окружения
   *
   * @return Поиск по наличию правил окружения
   */
  boolean useEnviromentRules();
}
