package mp.jprime.utils;

import org.apache.commons.lang3.StringUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.WeekFields;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Набор функций обработки дат
 */
public final class JPDateUtils {
  private static final WeekFields RU_WEEKS = WeekFields.of(DayOfWeek.MONDAY, 1);

  private static final JPDateUtils INSTANCE = new JPDateUtils();

  private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_DATE;
  private static final Map<String, DateTimeFormatter> FORMATTERS = new ConcurrentHashMap<>();

  private JPDateUtils() {
  }

  public static JPDateUtils getInstance() {
    return INSTANCE;
  }

  /**
   * Транформирует дату в строку в переданном формате
   *
   * @param date   Дата
   * @param format Формат (например dd.MM.yyyy)
   * @return Дата строкой в переданном формате
   */
  public static String dateToFormat(LocalDate date, String format) {
    return temporalToFormat(date, format);
  }

  /**
   * Транформирует дату и время в строку в переданном формате
   *
   * @param date   Дата и время
   * @param format Формат (например dd.MM.yyyy'T'HH:mm:ss)
   * @return Дата и время строкой в переданном формате
   */
  public static String dateTimeToFormat(LocalDateTime date, String format) {
    return temporalToFormat(date, format);
  }

  /**
   * Возвращает номер недели в месяца
   *
   * @param date Дата
   * @return Номер недели
   */
  public static int getWeekOfMonthNumber(LocalDate date) {
    return date.get(RU_WEEKS.weekOfMonth());
  }

  private static String temporalToFormat(Temporal date, String format) {
    if (date == null) {
      return "";
    }

    DateTimeFormatter df = StringUtils.isBlank(format)
        ? DEFAULT_FORMATTER
        : FORMATTERS.computeIfAbsent(format, DateTimeFormatter::ofPattern);

    return df.format(date);
  }
}
