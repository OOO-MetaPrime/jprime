package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * Double -> Long
 */
@Service
public final class DoubleToLongParser extends BaseTypeParser<Double, Long> {
  @Override
  public Long parse(Double value) {
    return value != null ? value.longValue() : null;
  }

  @Override
  public Class<Double> getInputType() {
    return Double.class;
  }

  @Override
  public Class<Long> getOutputType() {
    return Long.class;
  }
}