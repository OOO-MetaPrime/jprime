package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * Long -> Long
 */
@Service
public final class LongToLongParser extends BaseTypeParser<Long, Long> {
  @Override
  public Long parse(Long value) {
    return value;
  }

  @Override
  public Class<Long> getInputType() {
    return Long.class;
  }

  @Override
  public Class<Long> getOutputType() {
    return Long.class;
  }
}