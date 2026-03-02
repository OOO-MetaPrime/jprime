package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * Boolean -> Integer
 */
@Service
public final class BooleanToIntegerParser extends BaseTypeParser<Boolean, Integer> {
  @Override
  public Integer parse(Boolean value) {
    return value != null && value ? 1 : 0;
  }

  @Override
  public Class<Boolean> getInputType() {
    return Boolean.class;
  }

  @Override
  public Class<Integer> getOutputType() {
    return Integer.class;
  }
}