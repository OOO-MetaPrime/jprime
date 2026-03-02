package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Date -> LocalDate
 */
@Service
public final class DateToLocalDateTimeParser extends BaseTypeParser<Date, LocalDateTime> {
  @Override
  public LocalDateTime parse(Date value) {
    return value != null ? value.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(): null;
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
