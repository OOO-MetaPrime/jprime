package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * Float -> Long
 */
@Service
public final class FloatToLongParser extends BaseTypeParser<Float, Long> {
  @Override
  public Long parse(Float value) {
    return value != null ? value.longValue() : null;
  }

  @Override
  public Class<Float> getInputType() {
    return Float.class;
  }

  @Override
  public Class<Long> getOutputType() {
    return Long.class;
  }
}