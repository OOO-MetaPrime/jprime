package mp.jprime.schedule.editor;

import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.lang.JPMap;
import mp.jprime.schedule.JpScheduleTask;

import java.util.UUID;

/**
 * Задача, выполняющаяся по расписанию
 */
public interface JpScheduleEditorTask extends JpScheduleTask {
  /**
   * Признак отключения
   *
   * @return Да/Нет
   */
  boolean isDisable();

  /**
   * Настройки запуска
   *
   * @return Настройки запуска
   */
  JpScheduleEditorCron getCron();

  static JpScheduleEditorTask of(UUID code, boolean disable, String name, String description,
                                 String catalogCode, String executorCode, JPMap paramValues,
                                 JpScheduleEditorCron cron) {
    return new JpScheduleEditorTask() {
      @Override
      public UUID getCode() {
        return code;
      }

      @Override
      public boolean isDisable() {
        return disable;
      }

      @Override
      public String getName() {
        return name;
      }

      @Override
      public String getDescription() {
        return description;
      }

      @Override
      public String getCatalogCode() {
        return catalogCode;
      }

      @Override
      public String getExecutorCode() {
        return executorCode;
      }

      @Override
      public JPMap getParamValues() {
        return paramValues != null ? paramValues : JPData.empty();
      }

      @Override
      public JpScheduleEditorCron getCron() {
        return cron;
      }
    };
  }
}
