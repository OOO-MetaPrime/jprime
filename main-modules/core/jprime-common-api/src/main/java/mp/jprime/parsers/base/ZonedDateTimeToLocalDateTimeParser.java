package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.TimeZone;

/**
 * ZonedDateTime -> LocalDateTime
 */
@Service
public final class ZonedDateTimeToLocalDateTimeParser extends BaseTypeParser<ZonedDateTime, LocalDateTime> {
  @Override
  public LocalDateTime parse(ZonedDateTime value) {
    return value == null ? null : value.withZoneSameInstant(TimeZone.getDefault().toZoneId()).toLocalDateTime();
  }

  @Override
  public Class<ZonedDateTime> getInputType() {
    return ZonedDateTime.class;
  }

  @Override
  public Class<LocalDateTime> getOutputType() {
    return LocalDateTime.class;
  }
}
