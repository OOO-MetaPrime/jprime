package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * java.sql.Date -> LocalDate
 */
@Service
public final class SqlDateToLocalDateParser extends BaseTypeParser<Date, LocalDate> {
  @Override
  public LocalDate parse(Date value) {
    return value != null ? new Timestamp(value.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
  }

  @Override
  public Class<Date> getInputType() {
    return Date.class;
  }

  @Override
  public Class<LocalDate> getOutputType() {
    return LocalDate.class;
  }
}
