package mp.jprime.schedule;

import mp.jprime.security.AuthInfo;

import java.util.Collection;
import java.util.UUID;

/**
 * Задача, выполняющаяся по расписанию
 */
public interface JpScheduleService {
  /**
   * Список всех каталогов
   *
   * @return Список всех каталогов
   */
  Collection<JpScheduleTaskCatalog> getCatalogs();

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

  /**
   * Список всех задач
   *
   * @return Список всех задач
   */
  Collection<JpScheduleTask> getTasks();

  /**
   * Добавление задачи в планировщик
   *
   * @param task Описание задачи
   */
  void schedule(JpScheduleTask task);

  /**
   * Добавление задачи в планировщик
   *
   * @param code        Код задачи
   * @param name        Название задачи
   * @param description Описание
   * @param catalogCode Код каталог
   * @param cron        Периодичность задачи
   * @param execute     Логика задачи
   */
  default void schedule(UUID code, String name, String description, String catalogCode, JpScheduleCron cron, Runnable execute) {
    schedule(code, name, description, catalogCode, cron, execute, true);
  }

  /**
   * Добавление задачи в планировщик
   *
   * @param code        Код задачи
   * @param name        Название задачи
   * @param description Описание
   * @param catalogCode Код каталог
   * @param cron        Периодичность задачи
   * @param execute     Логика задачи
   * @param actionLog   Признак использования лога
   */
  void schedule(UUID code, String name, String description, String catalogCode, JpScheduleCron cron, Runnable execute, boolean actionLog);

  /**
   * Добавление задачи в планировщик
   *
   * @param name        Название задачи
   * @param catalogCode Код каталог
   * @param cron        Периодичность задачи
   * @param execute     Логика задачи
   */
  default void schedule(String name, String catalogCode, JpScheduleCron cron, Runnable execute) {
    schedule(name, null, catalogCode, cron, execute, true);
  }

  /**
   * Добавление задачи в планировщик
   *
   * @param name        Название задачи
   * @param catalogCode Код каталог
   * @param cron        Периодичность задачи
   * @param execute     Логика задачи
   * @param actionLog   Признак использования лога
   */
  default void schedule(String name, String catalogCode, JpScheduleCron cron, Runnable execute, boolean actionLog) {
    schedule(name, null, catalogCode, cron, execute, actionLog);
  }

  /**
   * Добавление задачи в планировщик
   *
   * @param name        Название задачи
   * @param description Описание
   * @param catalogCode Код каталог
   * @param cron        Периодичность задачи
   * @param execute     Логика задачи
   */
  default void schedule(String name, String description, String catalogCode, JpScheduleCron cron, Runnable execute) {
    schedule(name, description, catalogCode, cron, execute, true);
  }

  /**
   * Добавление задачи в планировщик
   *
   * @param name        Название задачи
   * @param description Описание
   * @param catalogCode Код каталог
   * @param cron        Периодичность задачи
   * @param execute     Логика задачи
   * @param actionLog   Признак использования лога
   */
  default void schedule(String name, String description, String catalogCode, JpScheduleCron cron, Runnable execute, boolean actionLog) {
    schedule(UUID.randomUUID(), name, description, catalogCode, cron, execute, actionLog);
  }

  /**
   * Признак выполняющейся задачи
   *
   * @param code Код задачи
   * @return Да/Нет
   */
  boolean isRunning(UUID code);

  /**
   * Удаление задачи
   *
   * @param code Код задачи
   */
  void remove(UUID code);

  /**
   * Запуск задачи
   *
   * @param code Код задачи
   * @param auth AuthIfo
   * @return Описание задачи
   */
  JpScheduleTask start(UUID code, AuthInfo auth);

  /**
   * Остановка задачи
   *
   * @param code Код задачи
   * @param auth AuthInfo
   * @return Описание задачи
   */
  JpScheduleTask stop(UUID code, AuthInfo auth);

  /**
   * Отмена задачи из планировщика
   *
   * @param code Код задачи
   */
  void stop(UUID code);
}
