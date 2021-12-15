package mp.jprime.parsers.base;

import mp.jprime.formats.DateFormat;
import mp.jprime.lang.JPDateTimeRange;
import mp.jprime.lang.JPRange;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

/**
 * String -> JPDateTimeRange
 */
@Service
public class StringToJPDateTimeRange implements TypeParser<String, JPDateTimeRange> {

  private static final DateTimeFormatter LOCAL_DATE_TIME = new DateTimeFormatterBuilder()
      .appendPattern("yyyy-MM-dd HH:mm:ss")
      .optionalStart()
      .appendPattern(".")
      .appendFraction(ChronoField.NANO_OF_SECOND, 1, 6, false)
      .optionalEnd()
      .toFormatter();

  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  @Override
  public JPDateTimeRange parse(String value) {
    if (value == null || value.isEmpty()) {
      return null;
    }
    if (value.equals(JPRange.EMPTY)) {
      return JPDateTimeRange.emptyRange();
    }

    boolean lowerClose = value.charAt(0) == '[';
    boolean upperClose = value.charAt(value.length() - 1) == ']';
    int delim = value.indexOf(',');

    if (delim == -1) {
      throw new IllegalArgumentException("Cannot find comma character");
    }

    String lowerStr = value.substring(1, delim);
    String upperStr = value.substring(delim + 1, value.length() - 1);

    LocalDateTime lower = null;
    LocalDateTime upper = null;

    if (!(lowerStr.length() == 0 || lowerStr.endsWith(JPRange.INFINITY))) {
      lower = LocalDateTime.parse(lowerStr, DateFormat.LOCAL_DATETIME_FORMAT);
    }

    if (!(upperStr.length() == 0 || upperStr.endsWith(JPRange.INFINITY))) {
      upper = LocalDateTime.parse(upperStr, DateFormat.LOCAL_DATETIME_FORMAT);
    }

    return JPDateTimeRange.create(lower, upper, lowerClose, upperClose);
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  @Override
  public Class<String> getInputType() {
    return String.class;
  }

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  @Override
  public Class<JPDateTimeRange> getOutputType() {
    return JPDateTimeRange.class;
  }
}
