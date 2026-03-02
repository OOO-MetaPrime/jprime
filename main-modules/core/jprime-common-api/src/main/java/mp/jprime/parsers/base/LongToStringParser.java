package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * Long -> String
 */
@Service
public final class LongToStringParser extends BaseTypeParser<Long, String> {
  @Override
  public String parse(Long value) {
    return value == null ? null : Long.toString(value);
  }

  @Override
  public Class<Long> getInputType() {
    return Long.class;
  }

  @Override
  public Class<String> getOutputType() {
    return String.class;
  }
}
