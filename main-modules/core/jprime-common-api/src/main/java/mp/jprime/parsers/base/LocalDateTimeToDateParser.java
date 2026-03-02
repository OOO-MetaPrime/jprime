package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * LocalDateTime -> Date
 */
@Service
public final class LocalDateTimeToDateParser extends BaseTypeParser<LocalDateTime, Date> {
  @Override
  public Date parse(LocalDateTime value) {
    return value == null ? null : Date.from(value.atZone(ZoneId.systemDefault()).toInstant());
  }

  @Override
  public Class<LocalDateTime> getInputType() {
    return LocalDateTime.class;
  }

  @Override
  public Class<Date> getOutputType() {
    return Date.class;
  }
}

