package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.TimeZone;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.temporal.ChronoField.*;

/**
 * String -> LocalDateTime
 */
@Service
public final class StringToLocalDateTimeParser extends BaseTypeParser<String, LocalDateTime> {
  private static final DateTimeFormatter LOCAL_DATETIME_FORMAT = new DateTimeFormatterBuilder()
      .parseCaseInsensitive()
      .append(ISO_LOCAL_DATE)
      .optionalStart()
      .appendLiteral('T')
      .appendValue(HOUR_OF_DAY, 2)
      .appendLiteral(':')
      .appendValue(MINUTE_OF_HOUR, 2)
      .appendLiteral(':')
      .appendValue(SECOND_OF_MINUTE, 2)
      .optionalEnd()
      .optionalStart()
      .appendPattern(".SSS")
      .optionalEnd()
      .optionalStart()
      .appendPattern("Z")
      .optionalEnd()
      .parseDefaulting(HOUR_OF_DAY, 0)
      .parseDefaulting(MINUTE_OF_HOUR, 0)
      .parseDefaulting(SECOND_OF_MINUTE, 0)
      .toFormatter()
      .withZone(TimeZone.getDefault().toZoneId());
  @Override
  public LocalDateTime parse(String value) {
    if (value == null || value.trim().isEmpty()) {
      return null;
    }
    ZonedDateTime zdt = ZonedDateTime.parse(value, LOCAL_DATETIME_FORMAT);
    return zdt == null ? null : zdt.withZoneSameInstant(TimeZone.getDefault().toZoneId()).toLocalDateTime();
  }

  @Override
  public Class<String> getInputType() {
    return String.class;
  }

  @Override
  public Class<LocalDateTime> getOutputType() {
    return LocalDateTime.class;
  }
}
