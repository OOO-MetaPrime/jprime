package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * Float -> Double
 */
@Service
public final class FloatToDoubleParser extends BaseTypeParser<Float, Double> {
  @Override
  public Double parse(Float value) {
    return value != null ? value.doubleValue() : null;
  }

  @Override
  public Class<Float> getInputType() {
    return Float.class;
  }

  @Override
  public Class<Double> getOutputType() {
    return Double.class;
  }
}