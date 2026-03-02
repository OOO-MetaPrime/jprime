package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * LocalDate -> Date
 */
@Service
public final class LocalDateToDateParser extends BaseTypeParser<LocalDate, Date> {
  @Override
  public Date parse(LocalDate value) {
    return value == null ? null : Date.from(value.atStartOfDay(ZoneId.systemDefault()).toInstant());
  }

  @Override
  public Class<LocalDate> getInputType() {
    return LocalDate.class;
  }

  @Override
  public Class<Date> getOutputType() {
    return Date.class;
  }
}

