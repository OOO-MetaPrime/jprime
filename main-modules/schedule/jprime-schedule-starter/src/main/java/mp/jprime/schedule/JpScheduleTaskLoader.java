package mp.jprime.schedule;

import java.util.Collection;

/**
 * Загрузка задач
 */
public interface JpScheduleTaskLoader {
  /**
   * Список задач
   *
   * @return Список задач
   */
  Collection<JpScheduleTask> getTasks();
}
