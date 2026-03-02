package mp.jprime.parsers.base;

import mp.jprime.lang.JPIntegerArray;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Integer -> JPIntegerArray
 */
@Service
public final class IntegerToJPIntegerArray extends BaseTypeParser<Integer, JPIntegerArray> {

  @Override
  public JPIntegerArray parse(Integer value) {
    if (value == null) {
      return null;
    }
    return JPIntegerArray.of(Collections.singletonList(value));
  }

  @Override
  public Class<Integer> getInputType() {
    return Integer.class;
  }

  @Override
  public Class<JPIntegerArray> getOutputType() {
    return JPIntegerArray.class;
  }
}
