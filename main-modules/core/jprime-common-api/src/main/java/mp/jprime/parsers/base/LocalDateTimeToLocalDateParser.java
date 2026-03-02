package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * LocalDateTime -> LocalDate
 */
@Service
public final class LocalDateTimeToLocalDateParser extends BaseTypeParser<LocalDateTime, LocalDate> {
  @Override
  public LocalDate parse(LocalDateTime value) {
    return value == null ? null : value.toLocalDate();
  }

  @Override
  public Class<LocalDateTime> getInputType() {
    return LocalDateTime.class;
  }

  @Override
  public Class<LocalDate> getOutputType() {
    return LocalDate.class;
  }
}
