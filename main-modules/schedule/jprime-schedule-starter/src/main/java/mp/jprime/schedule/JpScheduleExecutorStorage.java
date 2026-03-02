package mp.jprime.schedule;

import java.util.Collection;

/**
 * Хранилище настроек логики выполнения
 */
public interface JpScheduleExecutorStorage {
  /**
   * Список всех алгоритмов
   *
   * @return Список всех алгоритмов
   */
  Collection<JpScheduleExecutor> getExecutors();

  /**
   * Возвращает алгоритм по его коду
   *
   * @param code Код
   * @return Алгоритм
   */
  JpScheduleExecutor getExecutor(String code);
}
