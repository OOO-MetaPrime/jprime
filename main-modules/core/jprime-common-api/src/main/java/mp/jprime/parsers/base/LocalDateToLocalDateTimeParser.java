package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * LocalDate -> LocalDateTime
 */
@Service
public final class LocalDateToLocalDateTimeParser extends BaseTypeParser<LocalDate, LocalDateTime> {
  @Override
  public LocalDateTime parse(LocalDate value) {
    return value == null ? null : value.atStartOfDay();
  }

  @Override
  public Class<LocalDate> getInputType() {
    return LocalDate.class;
  }

  @Override
  public Class<LocalDateTime> getOutputType() {
    return LocalDateTime.class;
  }
}