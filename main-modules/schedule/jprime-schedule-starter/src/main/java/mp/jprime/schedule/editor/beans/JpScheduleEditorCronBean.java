package mp.jprime.schedule.editor.beans;

import mp.jprime.schedule.JpScheduleType;
import mp.jprime.schedule.beans.JpScheduleCronBean;
import mp.jprime.schedule.editor.JpScheduleEditorCron;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

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
public final class JpScheduleEditorCronBean extends JpScheduleCronBean implements JpScheduleEditorCron {
  private final boolean useExpression;
  private final SecondConfig secondConfig;
  private final MinuteConfig minuteConfig;
  private final HourConfig hourConfig;
  private final DayConfig dayConfig;
  private final MonthConfig monthConfig;
  private final DayOfWeekConfig dayOfWeekConfig;

  private JpScheduleEditorCronBean(String expression,
                                   boolean useExpression,
                                   SecondConfig secondConfig,
                                   MinuteConfig minuteConfig,
                                   HourConfig hourConfig,
                                   DayConfig dayConfig,
                                   MonthConfig monthConfig,
                                   DayOfWeekConfig dayOfWeekConfig) {
    super(expression);
    this.useExpression = useExpression;
    this.secondConfig = secondConfig;
    this.minuteConfig = minuteConfig;
    this.hourConfig = hourConfig;
    this.dayConfig = dayConfig;
    this.monthConfig = monthConfig;
    this.dayOfWeekConfig = dayOfWeekConfig;

  }

  public static JpScheduleEditorCron of(String expression) {
    return new JpScheduleEditorCronBean(
        expression, true,
        null, null, null, null, null, null
    );
  }

  public static JpScheduleEditorCron of(SecondConfig secondConfig,
                                        MinuteConfig minuteConfig,
                                        HourConfig hourConfig,
                                        DayConfig dayConfig,
                                        MonthConfig monthConfig,
                                        DayOfWeekConfig dayOfWeekConfig) {
    return new JpScheduleEditorCronBean(
        toExpression(secondConfig, minuteConfig, hourConfig, dayConfig, monthConfig, dayOfWeekConfig), false,
        secondConfig, minuteConfig, hourConfig, dayConfig, monthConfig, dayOfWeekConfig
    );
  }

  public static SecondConfig ofSecondConfig(JpScheduleType type, Integer every, Collection<Integer> specific) {
    return new SecondConfigRecord(type != null ? type : JpScheduleType.EVERY,
        every,
        specific != null ? Collections.unmodifiableCollection(specific) : Collections.emptyList());
  }

  public static MinuteConfig ofMinuteConfig(JpScheduleType type, Integer every, Collection<Integer> specific) {
    return new MinuteConfigRecord(type != null ? type : JpScheduleType.EVERY,
        every,
        specific != null ? Collections.unmodifiableCollection(specific) : Collections.emptyList());
  }

  public static HourConfig ofHourConfig(JpScheduleType type, Integer every, Collection<Integer> specific) {
    return new HourConfigRecord(type != null ? type : JpScheduleType.EVERY,
        every,
        specific != null ? Collections.unmodifiableCollection(specific) : Collections.emptyList());
  }

  public static DayConfig ofDayConfig(JpScheduleType type, Integer every, Collection<Integer> specific) {
    return new DayConfigRecord(type != null ? type : JpScheduleType.EVERY,
        every,
        specific != null ? Collections.unmodifiableCollection(specific) : Collections.emptyList());
  }

  public static MonthConfig ofMonthConfig(JpScheduleType type, Integer every, Collection<Integer> specific) {
    return new MonthConfigRecord(type != null ? type : JpScheduleType.EVERY,
        every,
        specific != null ? Collections.unmodifiableCollection(specific) : Collections.emptyList());
  }

  public static DayOfWeekConfig ofDayOfWeekConfig(JpScheduleType type, Integer every, Collection<Integer> specific) {
    return new DayOfWeekConfigRecord(type != null ? type : JpScheduleType.EVERY,
        every,
        specific != null ? Collections.unmodifiableCollection(specific) : Collections.emptyList());
  }

  @Override
  public boolean isUseExpression() {
    return useExpression;
  }

  @Override
  public SecondConfig getSecondConfig() {
    return secondConfig;
  }

  @Override
  public MinuteConfig getMinuteConfig() {
    return minuteConfig;
  }

  @Override
  public HourConfig getHourConfig() {
    return hourConfig;
  }

  @Override
  public DayConfig getDayConfig() {
    return dayConfig;
  }

  @Override
  public MonthConfig getMonthConfig() {
    return monthConfig;
  }

  @Override
  public DayOfWeekConfig getDayOfWeekConfig() {
    return dayOfWeekConfig;
  }

  private record SecondConfigRecord(JpScheduleType getType, Integer getEvery,
                                    Collection<Integer> getSpecific) implements SecondConfig {

  }

  private record MinuteConfigRecord(JpScheduleType getType, Integer getEvery,
                                    Collection<Integer> getSpecific) implements MinuteConfig {

  }

  private record HourConfigRecord(JpScheduleType getType, Integer getEvery,
                                  Collection<Integer> getSpecific) implements HourConfig {

  }

  private record DayConfigRecord(JpScheduleType getType, Integer getEvery,
                                 Collection<Integer> getSpecific) implements DayConfig {

  }

  private record MonthConfigRecord(JpScheduleType getType, Integer getEvery,
                                   Collection<Integer> getSpecific) implements MonthConfig {

  }

  private record DayOfWeekConfigRecord(JpScheduleType getType, Integer getEvery,
                                       Collection<Integer> getSpecific) implements DayOfWeekConfig {

  }

  private static String toExpression(SecondConfig secondConfig,
                                     MinuteConfig minuteConfig,
                                     HourConfig hourConfig,
                                     DayConfig dayConfig,
                                     MonthConfig monthConfig,
                                     DayOfWeekConfig dayOfWeekConfig) {
    return (toExpression(secondConfig) + " " +
        toExpression(minuteConfig) + " " +
        toExpression(hourConfig) + " " +
        toExpression(dayConfig) + " " +
        toExpression(monthConfig) + " " +
        toExpression(dayOfWeekConfig)).trim();
  }

  private static String toExpression(Config config) {
    if (config == null) {
      return "*";
    }

    return switch (config.getType()) {
      case EVERY -> "*";
      case EVERY_N -> "*/" + config.getEvery();
      case SPECIFIC -> join(config.getSpecific());
    };
  }

  private static String join(Collection<Integer> values) {
    if (values == null || values.isEmpty()) {
      return "*";
    }
    return values.stream()
        .map(String::valueOf)
        .collect(Collectors.joining(","));
  }
}
