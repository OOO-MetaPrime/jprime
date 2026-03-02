package mp.jprime.schedule.editor;

import mp.jprime.schedule.JpScheduleCron;
import mp.jprime.schedule.JpScheduleType;
import mp.jprime.schedule.editor.beans.JpScheduleEditorCronBean;

import java.util.Collection;

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
public interface JpScheduleEditorCron extends JpScheduleCron {
  static JpScheduleEditorCron of(String expression) {
    return JpScheduleEditorCronBean.of(expression);
  }

  static JpScheduleEditorCron of(SecondConfig secondConfig,
                                 MinuteConfig minuteConfig,
                                 HourConfig hourConfig,
                                 DayConfig dayConfig,
                                 MonthConfig monthConfig,
                                 DayOfWeekConfig dayOfWeekConfig) {
    return JpScheduleEditorCronBean.of(secondConfig, minuteConfig, hourConfig, dayConfig, monthConfig, dayOfWeekConfig);
  }

  static SecondConfig ofSecondConfig(JpScheduleType type, Integer every, Collection<Integer> specific) {
    return JpScheduleEditorCronBean.ofSecondConfig(type, every, specific);
  }

  static MinuteConfig ofMinuteConfig(JpScheduleType type, Integer every, Collection<Integer> specific) {
    return JpScheduleEditorCronBean.ofMinuteConfig(type, every, specific);
  }

  static HourConfig ofHourConfig(JpScheduleType type, Integer every, Collection<Integer> specific) {
    return JpScheduleEditorCronBean.ofHourConfig(type, every, specific);
  }

  static DayConfig ofDayConfig(JpScheduleType type, Integer every, Collection<Integer> specific) {
    return JpScheduleEditorCronBean.ofDayConfig(type, every, specific);
  }

  static MonthConfig ofMonthConfig(JpScheduleType type, Integer every, Collection<Integer> specific) {
    return JpScheduleEditorCronBean.ofMonthConfig(type, every, specific);
  }

  static DayOfWeekConfig ofDayOfWeekConfig(JpScheduleType type, Integer every, Collection<Integer> specific) {
    return JpScheduleEditorCronBean.ofDayOfWeekConfig(type, every, specific);
  }

  /**
   * Признак использования expression вместо настроек cron
   */
  boolean isUseExpression();

  /**
   * Настройки секунд
   */
  SecondConfig getSecondConfig();

  /**
   * Настройки минут
   */
  MinuteConfig getMinuteConfig();

  /**
   * Настройки часов
   */
  HourConfig getHourConfig();

  /**
   * Настройки дней
   */
  DayConfig getDayConfig();

  /**
   * Настройки месяца
   */
  MonthConfig getMonthConfig();

  /**
   * Настройки дня недели
   */
  DayOfWeekConfig getDayOfWeekConfig();

  /**
   * Настройки месяца
   */
  interface SecondConfig extends Config {

  }

  /**
   * Настройки минут
   */
  interface MinuteConfig extends Config {

  }

  /**
   * Настройки часов
   */
  interface HourConfig extends Config {

  }

  /**
   * Настройки дней
   */
  interface DayConfig extends Config {

  }

  /**
   * Настройки месяца
   */
  interface MonthConfig extends Config {

  }

  /**
   * Настройки дня недели
   */
  interface DayOfWeekConfig extends Config {

  }

  /**
   * Настройки секунд
   */
  interface Config {
    /**
     * Тип настройки
     *
     * @return тип настройка
     */
    JpScheduleType getType();

    /**
     * Каждые N
     *
     * @return Каждые N
     */
    Integer getEvery();

    /**
     * Явно указанные значения
     *
     * @return Явно указанные значения
     */
    Collection<Integer> getSpecific();
  }
}
