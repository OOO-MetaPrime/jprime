package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * Boolean -> Boolean
 */
@Service
public final class BooleanToBooleanParser extends BaseTypeParser<Boolean, Boolean> {
  @Override
  public Boolean parse(Boolean value) {
    return value;
  }

  @Override
  public Class<Boolean> getInputType() {
    return Boolean.class;
  }

  @Override
  public Class<Boolean> getOutputType() {
    return Boolean.class;
  }
}