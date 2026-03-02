package mp.jprime.parsers.base;

import mp.jprime.lang.JPIntegerArray;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * Integer[] -> JPIntegerArray
 */
@Service
public final class IntegerArrayToJPIntegerArray extends BaseTypeParser<Integer[], JPIntegerArray> {
  @Override
  public JPIntegerArray parse(Integer[] value) {
    return JPIntegerArray.of(value);
  }

  @Override
  public Class<Integer[]> getInputType() {
    return Integer[].class;
  }

  @Override
  public Class<JPIntegerArray> getOutputType() {
    return JPIntegerArray.class;
  }
}
