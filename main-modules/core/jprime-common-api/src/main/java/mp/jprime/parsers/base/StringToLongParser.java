package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * String -> Long
 */
@Service
public final class StringToLongParser extends BaseTypeParser<String, Long> {
  @Override
  public Long parse(String value) {
    return value == null || value.isEmpty() ? null : Long.parseLong(value.trim());
  }

  @Override
  public Class<String> getInputType() {
    return String.class;
  }

  @Override
  public Class<Long> getOutputType() {
    return Long.class;
  }
}
