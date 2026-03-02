package mp.jprime.parsers.base;

import mp.jprime.formats.DateFormat;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.TimeZone;

/**
 * LocalDateTime -> String
 */
@Service
public final class LocalDateTimeToStringParser extends BaseTypeParser<LocalDateTime, String> {
  @Override
  public String parse(LocalDateTime value) {
    if (value == null) {
      return null;
    }
    return DateFormat.LOCAL_DATETIME_FORMAT.format(ZonedDateTime.of(value, TimeZone.getDefault().toZoneId()));
  }

  @Override
  public Class<LocalDateTime> getInputType() {
    return LocalDateTime.class;
  }

  @Override
  public Class<String> getOutputType() {
    return String.class;
  }
}
