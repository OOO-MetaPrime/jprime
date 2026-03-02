package mp.jprime.schedule;

import mp.jprime.schedule.beans.JpScheduleCronBean;

/**
 * Настройки запуска
 * ┌───────────── second (0-59)
 * │ ┌───────────── minute (0 - 59)
 * │ │ ┌───────────── hour (0 - 23)
 * │ │ │ ┌───────────── day of the month (1 - 31)
 * │ │ │ │ ┌───────────── month (1 - 12) (or JAN-DEC)
 * │ │ │ │ │ ┌───────────── day of the week (0 - 7)
 * │ │ │ │ │ │          (or MON-SUN -- 0 or 7 is Sunday)
 * │ │ │ │ │ │
 * * * * * * *
 */
public interface JpScheduleCron {
  /**
   * Описание крона
   *
   * @return Описание крона
   */
  String getDescription();

  /**
   * Настройки запуска
   *
   * @return Настройка запуска
   */
  String getExpression();

  static JpScheduleCron of(String expression) {
    return JpScheduleCronBean.of(expression);
  }
}
