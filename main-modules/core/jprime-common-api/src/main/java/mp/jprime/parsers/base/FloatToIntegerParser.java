package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * Float -> Integer
 */
@Service
public final class FloatToIntegerParser extends BaseTypeParser<Float, Integer> {
  @Override
  public Integer parse(Float value) {
    return value != null ? value.intValue() : null;
  }

  @Override
  public Class<Float> getInputType() {
    return Float.class;
  }

  @Override
  public Class<Integer> getOutputType() {
    return Integer.class;
  }
}