package mp.jprime.globalsettings;

import java.util.Collection;

/**
 * Загрузчик каталогов глобальный настроек
 */
public interface JpGlobalPropertyCatalogLoader {
  /**
   * Возвращает описание каталогов глобальный настроек
   *
   * @return Описание каталогов глобальный настроек
   */
  Collection<JpGlobalPropertyCatalog> getCatalogs();
}
