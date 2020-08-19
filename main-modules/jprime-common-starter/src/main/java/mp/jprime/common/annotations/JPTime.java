package mp.jprime.common.annotations;

import java.time.DayOfWeek;

/**
 * Время
 */
public @interface JPTime {
  /**
   * Дни недели
   *
   * @return Дни недели
   */
  DayOfWeek[] daysOfWeek() default {};
  /**
   * Начало периода в формате HH:mm
   *
   * @return Начало периода
   */
  String fromTime() default "";

  /**
   * Окончание периода в формате HH:mm
   *
   * @return Окончание периода
   */
  String toTime() default "";
  /**
   * Начало периода в формате yyyy-MM-dd'T'HH:mm:ss
   *
   * @return Начало периода
   */
  String fromDateTime() default "";

  /**
   * Окончание периода в формате yyyy-MM-dd'T'HH:mm:ss
   *
   * @return Окончание периода
   */
  String toDateTime() default "";
}
