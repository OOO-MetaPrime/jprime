package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * Integer -> Float
 */
@Service
public final class IntegerToFloatParser extends BaseTypeParser<Integer, Float> {
  @Override
  public Float parse(Integer value) {
    return value != null ? value.floatValue() : null;
  }

  @Override
  public Class<Integer> getInputType() {
    return Integer.class;
  }

  @Override
  public Class<Float> getOutputType() {
    return Float.class;
  }
}