package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * String -> Integer
 */
@Service
public final class StringToIntegerParser extends BaseTypeParser<String, Integer> {
  @Override
  public Integer parse(String value) {
    return value == null || value.isEmpty() ? null : Integer.parseInt(value.trim());
  }

  @Override
  public Class<String> getInputType() {
    return String.class;
  }

  @Override
  public Class<Integer> getOutputType() {
    return Integer.class;
  }
}
