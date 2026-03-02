package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * Integer -> String
 */
@Service
public final class IntegerToStringParser extends BaseTypeParser<Integer, String> {
  @Override
  public String parse(Integer value) {
    return value == null ? null : value.toString();
  }

  @Override
  public Class<Integer> getInputType() {
    return Integer.class;
  }

  @Override
  public Class<String> getOutputType() {
    return String.class;
  }
}

