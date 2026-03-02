package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * String -> Float
 */
@Service
public final class StringToFloatParser extends BaseTypeParser<String, Float> {
  @Override
  public Float parse(String value) {
    return value == null || value.isEmpty() ? null : Float.parseFloat(value.trim());
  }

  @Override
  public Class<String> getInputType() {
    return String.class;
  }

  @Override
  public Class<Float> getOutputType() {
    return Float.class;
  }
}
