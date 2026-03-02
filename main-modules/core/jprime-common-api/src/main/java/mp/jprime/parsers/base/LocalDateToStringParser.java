package mp.jprime.parsers.base;

import mp.jprime.formats.DateFormat;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * LocalDate -> String
 */
@Service
public final class LocalDateToStringParser extends BaseTypeParser<LocalDate, String> {
  @Override
  public String parse(LocalDate value) {
    return value == null ? null : DateFormat.LOCAL_DATE_FORMAT.format(value);
  }

  @Override
  public Class<LocalDate> getInputType() {
    return LocalDate.class;
  }

  @Override
  public Class<String> getOutputType() {
    return String.class;
  }
}
