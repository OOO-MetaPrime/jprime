package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * Integer -> Boolean
 */
@Service
public final class IntegerToBooleanParser extends BaseTypeParser<Integer, Boolean> {
  @Override
  public Boolean parse(Integer value) {
    return value != null && value == 1;
  }

  @Override
  public Class<Integer> getInputType() {
    return Integer.class;
  }

  @Override
  public Class<Boolean> getOutputType() {
    return Boolean.class;
  }
}