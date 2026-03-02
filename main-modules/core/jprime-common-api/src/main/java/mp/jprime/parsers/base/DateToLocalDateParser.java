package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Date -> LocalDate
 */
@Service
public final class DateToLocalDateParser extends BaseTypeParser<Date, LocalDate> {
  @Override
  public LocalDate parse(Date value) {
    return value != null ? value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(): null;
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
