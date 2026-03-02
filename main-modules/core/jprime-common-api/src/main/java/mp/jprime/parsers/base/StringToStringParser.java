package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * String -> String
 */
@Service
public final class StringToStringParser extends BaseTypeParser<String, String> {
  @Override
  public String parse(String value) {
    return value != null ? value.trim() : null;
  }

  @Override
  public Class<String> getInputType() {
    return String.class;
  }

  @Override
  public Class<String> getOutputType() {
    return String.class;
  }
}
