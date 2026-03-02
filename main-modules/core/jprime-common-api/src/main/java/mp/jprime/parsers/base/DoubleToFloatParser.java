package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * Double -> Float
 */
@Service
public final class DoubleToFloatParser extends BaseTypeParser<Double, Float> {
  @Override
  public Float parse(Double value) {
    return value != null ? value.floatValue() : null;
  }

  @Override
  public Class<Double> getInputType() {
    return Double.class;
  }

  @Override
  public Class<Float> getOutputType() {
    return Float.class;
  }
}