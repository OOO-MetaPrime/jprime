package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * Double -> Integer
 */
@Service
public final class DoubleToIntegerParser extends BaseTypeParser<Double, Integer> {
  @Override
  public Integer parse(Double value) {
    return value != null ? value.intValue() : null;
  }

  @Override
  public Class<Double> getInputType() {
    return Double.class;
  }

  @Override
  public Class<Integer> getOutputType() {
    return Integer.class;
  }
}