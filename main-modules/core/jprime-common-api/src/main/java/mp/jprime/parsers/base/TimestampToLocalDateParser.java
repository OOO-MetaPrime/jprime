package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * Timestamp -> LocalDate
 */
@Service
public final class TimestampToLocalDateParser extends BaseTypeParser<Timestamp, LocalDate> {
  @Override
  public LocalDate parse(Timestamp value) {
    return value != null ? value.toLocalDateTime().toLocalDate() : null;
  }

  @Override
  public Class<Timestamp> getInputType() {
    return Timestamp.class;
  }

  @Override
  public Class<LocalDate> getOutputType() {
    return LocalDate.class;
  }
}
