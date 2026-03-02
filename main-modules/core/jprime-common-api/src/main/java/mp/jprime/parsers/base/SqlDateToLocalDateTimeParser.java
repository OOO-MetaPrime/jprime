package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * java.sql.Date -> LocalDate
 */
@Service
public final class SqlDateToLocalDateTimeParser extends BaseTypeParser<Date, LocalDateTime> {
  @Override
  public LocalDateTime parse(Date value) {
    return value != null ? new Timestamp(value.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() : null;
  }

  @Override
  public Class<Date> getInputType() {
    return Date.class;
  }

  @Override
  public Class<LocalDateTime> getOutputType() {
    return LocalDateTime.class;
  }
}
