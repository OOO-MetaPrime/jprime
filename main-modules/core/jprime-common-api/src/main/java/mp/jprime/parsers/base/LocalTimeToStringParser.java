package mp.jprime.parsers.base;

import mp.jprime.formats.DateFormat;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

/**
 * LocalTime -> String
 */
@Service
public final class LocalTimeToStringParser extends BaseTypeParser<LocalTime, String> {
  @Override
  public String parse(LocalTime value) {
    return value == null ? null : DateFormat.LOCAL_TIME_FORMAT.format(value);
  }

  @Override
  public Class<LocalTime> getInputType() {
    return LocalTime.class;
  }

  @Override
  public Class<String> getOutputType() {
    return String.class;
  }
}
