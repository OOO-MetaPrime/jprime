package mp.jprime.schedule.editor;

import mp.jprime.security.AuthInfo;

import java.util.Collection;

/**
 * Динамическое изменение настроек задач
 */
public interface JpScheduleEditorService {
  /**
   * Возвращает все настроенные задачи
   *
   * @return Список настроенных задач
   */
  Collection<JpScheduleEditorTask> getTasks();

  /**
   * Создает или обновляет задачу
   *
   * @param task Задача
   * @param auth AuthInfo
   * @return Созданная задача
   */
  JpScheduleEditorTask save(JpScheduleEditorTask task, AuthInfo auth);
}
