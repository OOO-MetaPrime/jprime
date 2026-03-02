package mp.jprime.parsers.base;

import mp.jprime.formats.DateFormat;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

/**
 * String -> LocalTime
 */
@Service
public final class StringToLocalTimeParser extends BaseTypeParser<String, LocalTime> {
  @Override
  public LocalTime parse(String value) {
    if (value == null || value.trim().isEmpty()) {
      return null;
    }
    if (value.length() > 19) {
      value = value.substring(11, 19);
    }
    return LocalTime.parse(value, DateFormat.LOCAL_TIME_FORMAT);
  }

  @Override
  public Class<String> getInputType() {
    return String.class;
  }

  @Override
  public Class<LocalTime> getOutputType() {
    return LocalTime.class;
  }
}
