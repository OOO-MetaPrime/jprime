package mp.jprime.parsers.base;

import mp.jprime.lang.JPDateTimeRange;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeParseException;

/**
 * JPDateTimeRange -> String
 */
@Service
public final class JPDateTimeRangeToString implements TypeParser<JPDateTimeRange, String> {

  /**
   * Создание {@code LocalDateTime} диапазона из переданной строки:
   * <pre>{@code
   *     Range<LocalDateTime> closed = Range.localDateTimeRange("[2014-04-28 16:00:49,2015-04-28 16:00:49]");
   *     Range<LocalDateTime> quoted = Range.localDateTimeRange("[\"2014-04-28 16:00:49\",\"2015-04-28 16:00:49\"]");
   *    Range<LocalDateTime> iso = Range.localDateTimeRange("[\"2014-04-28T16:00:49.2358\",\"2015-04-28T16:00:49\"]");
   * }</pre>
   * <p>
   * Доступный формат даты:
   * <ul>
   * <li>yyyy-MM-dd HH:mm:ss[.SSSSSS]</li>
   * <li>yyyy-MM-dd'T'HH:mm:ss[.SSSSSS]</li>
   * </ul>
   *
   * @return The range of {@code LocalDateTime}s.
   * @throws DateTimeParseException when one of the bounds are invalid.
   */
  @Override
  public String parse(JPDateTimeRange value) {
    return value.asString();
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  @Override
  public Class<JPDateTimeRange> getInputType() {
    return JPDateTimeRange.class;
  }

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  @Override
  public Class<String> getOutputType() {
    return String.class;
  }
}
