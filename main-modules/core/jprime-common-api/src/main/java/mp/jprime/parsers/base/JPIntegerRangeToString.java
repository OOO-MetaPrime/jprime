package mp.jprime.parsers.base;

import mp.jprime.lang.JPIntegerRange;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * JPIntegerRange -> String
 */
@Service
public final class JPIntegerRangeToString extends BaseTypeParser<JPIntegerRange, String> {
  @Override
  public String parse(JPIntegerRange value) {
    return value.asString();
  }

  @Override
  public Class<JPIntegerRange> getInputType() {
    return JPIntegerRange.class;
  }

  @Override
  public Class<String> getOutputType() {
    return String.class;
  }
}
