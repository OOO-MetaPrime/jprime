package mp.jprime.schedule;

import java.util.Collection;

/**
 * Загрузчик каталогов задач, выполняемых по расписанию
 */
public interface JpScheduleTaskCatalogLoader {
  /**
   * Возвращает описание каталогов
   *
   * @return Описание каталогов
   */
  Collection<JpScheduleTaskCatalog> getCatalogs();
}
