package mp.jprime.parsers.base;


import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * OffsetDateTime -> LocalDateTime
 */
@Service
public final class OffsetDateTimeToLocalDateTimeParser extends BaseTypeParser<OffsetDateTime, LocalDateTime> {
  @Override
  public LocalDateTime parse(OffsetDateTime value) {
    return value != null ? value.toLocalDateTime() : null;
  }

  @Override
  public Class<OffsetDateTime> getInputType() {
    return OffsetDateTime.class;
  }

  @Override
  public Class<LocalDateTime> getOutputType() {
    return LocalDateTime.class;
  }
}
