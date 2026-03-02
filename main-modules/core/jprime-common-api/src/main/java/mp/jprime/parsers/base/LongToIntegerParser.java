package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * Long -> Integer
 */
@Service
public final class LongToIntegerParser extends BaseTypeParser<Long, Integer> {
  @Override
  public Integer parse(Long value) {
    return value == null ? null : value.intValue();
  }

  @Override
  public Class<Long> getInputType() {
    return Long.class;
  }

  @Override
  public Class<Integer> getOutputType() {
    return Integer.class;
  }
}
