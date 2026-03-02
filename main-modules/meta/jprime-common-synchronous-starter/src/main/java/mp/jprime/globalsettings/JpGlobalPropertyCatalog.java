package mp.jprime.globalsettings;

import java.util.Collection;

/**
 * Каталог глобальный настроек
 */
public interface JpGlobalPropertyCatalog {
  /**
   * Код каталога
   *
   * @return Код каталога
   */
  String getCatalog();

  /**
   * Название каталога
   *
   * @return Название
   */
  String getName();

  /**
   * Роли доступа
   *
   * @return Список ролей для доступа
   */
  Collection<String> getRoles();
}
