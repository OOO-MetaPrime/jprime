package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * Long -> Double
 */
@Service
public final class LongToDoubleParser extends BaseTypeParser<Long, Double> {
  @Override
  public Double parse(Long value) {
    return value != null ? value.doubleValue() : null;
  }

  @Override
  public Class<Long> getInputType() {
    return Long.class;
  }

  @Override
  public Class<Double> getOutputType() {
    return Double.class;
  }
}