package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * Float -> String
 */
@Service
public final class FloatToStringParser extends BaseTypeParser<Float, String> {
  @Override
  public String parse(Float value) {
    return value != null ? Float.toString(value) : null;
  }

  @Override
  public Class<Float> getInputType() {
    return Float.class;
  }

  @Override
  public Class<String> getOutputType() {
    return String.class;
  }
}