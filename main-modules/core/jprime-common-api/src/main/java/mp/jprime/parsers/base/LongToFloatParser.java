package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * Long -> Float
 */
@Service
public final class LongToFloatParser extends BaseTypeParser<Long, Float> {
  @Override
  public Float parse(Long value) {
    return value == null ? null : value.floatValue();
  }

  @Override
  public Class<Long> getInputType() {
    return Long.class;
  }

  @Override
  public Class<Float> getOutputType() {
    return Float.class;
  }
}