package mp.jprime.schedule;

import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.lang.JPMap;

import java.util.UUID;

/**
 * Задача, выполняющаяся по расписанию
 */
public interface JpScheduleTask {
  /**
   * Код задачи
   *
   * @return Код задача
   */
  UUID getCode();

  /**
   * Название задачи
   *
   * @return Название задачи
   */
  String getName();

  /**
   * Описание задачи
   *
   * @return Описание задачи
   */
  String getDescription();

  /**
   * Код каталога задачи
   *
   * @return Код каталога задачи
   */
  String getCatalogCode();

  /**
   * Код выполняемой логики
   *
   * @return Код логики
   */
  String getExecutorCode();

  /**
   * Параметры задачи
   *
   * @return Параметры задачи
   */
  JPMap getParamValues();

  /**
   * Настройки запуска
   *
   * @return Настройки запуска
   */
  JpScheduleCron getCron();

  static JpScheduleTask of(UUID code, String name, String catalogCode, JpScheduleCron cron) {
    return of(code, name, null, catalogCode, null, null, cron);
  }

  static JpScheduleTask of(UUID code, String name, String description, String catalogCode, JpScheduleCron cron) {
    return of(code, name, description, catalogCode, null, null, cron);
  }

  static JpScheduleTask of(UUID code, String name, String description,
                           String catalogCode, String executorCode, JPMap paramValues,
                           JpScheduleCron cron) {
    return new JpScheduleTask() {
      @Override
      public UUID getCode() {
        return code;
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
      public JpScheduleCron getCron() {
        return cron;
      }
    };
  }
}
