package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * Integer -> Integer
 */
@Service
public final class IntegerToIntegerParser extends BaseTypeParser<Integer, Integer> {
  @Override
  public Integer parse(Integer value) {
    return value;
  }

  @Override
  public Class<Integer> getInputType() {
    return Integer.class;
  }

  @Override
  public Class<Integer> getOutputType() {
    return Integer.class;
  }
}