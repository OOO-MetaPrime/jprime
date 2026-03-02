package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * Integer -> Double
 */
@Service
public final class IntegerToDoubleParser extends BaseTypeParser<Integer, Double> {
  @Override
  public Double parse(Integer value) {
    return value != null ? value.doubleValue() : null;
  }

  @Override
  public Class<Integer> getInputType() {
    return Integer.class;
  }

  @Override
  public Class<Double> getOutputType() {
    return Double.class;
  }
}