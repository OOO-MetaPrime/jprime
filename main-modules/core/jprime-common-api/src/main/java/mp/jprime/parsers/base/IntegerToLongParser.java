package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * Integer -> Long
 */
@Service
public final class IntegerToLongParser extends BaseTypeParser<Integer, Long> {
  @Override
  public Long parse(Integer value) {
    return value == null ? null : value.longValue();
  }

  @Override
  public Class<Integer> getInputType() {
    return Integer.class;
  }

  @Override
  public Class<Long> getOutputType() {
    return Long.class;
  }
}

