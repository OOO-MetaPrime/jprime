package mp.jprime.formats;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.TimeZone;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.temporal.ChronoField.*;

/**
 * Форматы дат
 */
public final class DateFormat {
  /**
   * Год-месяц-день
   */
  public static final String YYYYY_MM_DD = "yyyy-MM-dd";
  /**
   * Год-месяц-день чч:мм
   */
  public static final String YYYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
  /**
   * день-месяц-год
   */
  public static final String DD_MM_YYYY = "dd-MM-yyyy";
  /**
   * день.месяц.год
   */
  public static final String DDdMMdYYYY = "dd.MM.yyyy";
  /**
   * Формат ИСО 8601 (полное время)
   */
  public static final String ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
  /**
   * Формат ИСО 8601 - (год-месяц-день-час-минута)
   */
  public static final String ISO8601_YYYY_MM_DD_HH_MM = "yyyy-MM-dd'T'HH:mmZ";

  public static final DateTimeFormatter LOCAL_DATETIME_FORMAT = new DateTimeFormatterBuilder()
      .parseCaseInsensitive()
      .append(ISO_LOCAL_DATE)
      .appendLiteral('T')
      .appendValue(HOUR_OF_DAY, 2)
      .appendLiteral(':')
      .appendValue(MINUTE_OF_HOUR, 2)
      .appendLiteral(':')
      .appendValue(SECOND_OF_MINUTE, 2)
      .optionalStart()
      .appendPattern(".SSS")
      .optionalEnd()
      .optionalStart()
      .appendPattern("Z")
      .optionalEnd()
      .toFormatter()
      .withZone(TimeZone.getDefault().toZoneId());

  public static final DateTimeFormatter LOCAL_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

  public static final DateTimeFormatter LOCAL_TIME_FORMAT = DateTimeFormatter.ISO_LOCAL_TIME;
}
