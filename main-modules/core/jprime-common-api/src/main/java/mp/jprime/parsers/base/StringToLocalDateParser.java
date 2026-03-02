package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * String -> LocalDate
 */
@Service
public final class StringToLocalDateParser extends BaseTypeParser<String, LocalDate> {
  @Override
  public LocalDate parse(String value) {
    String[] tokens = value != null ? value.split("-") : null;
    if (tokens == null || tokens.length != 3) {
      return null;

    }
    String day = tokens[2];
    if (day.length() > 2) {
      day = day.substring(0, 2);
    }
    return LocalDate.of(
        Integer.parseInt(tokens[0]),
        Integer.parseInt(tokens[1]),
        Integer.parseInt(day)
    );
  }

  @Override
  public Class<String> getInputType() {
    return String.class;
  }

  @Override
  public Class<LocalDate> getOutputType() {
    return LocalDate.class;
  }
}
