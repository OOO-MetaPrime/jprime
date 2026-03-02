package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Timestamp -> LocalDateTime
 */
@Service
public final class TimestampToLocalDateTimeParser extends BaseTypeParser<Timestamp, LocalDateTime> {
  @Override
  public LocalDateTime parse(Timestamp value) {
    return value != null ? value.toLocalDateTime() : null;
  }

  @Override
  public Class<Timestamp> getInputType() {
    return Timestamp.class;
  }

  @Override
  public Class<LocalDateTime> getOutputType() {
    return LocalDateTime.class;
  }
}
