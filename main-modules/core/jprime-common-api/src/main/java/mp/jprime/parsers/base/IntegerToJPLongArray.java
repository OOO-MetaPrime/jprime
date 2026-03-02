package mp.jprime.parsers.base;

import mp.jprime.lang.JPLongArray;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Integer -> JPLongArray
 */
@Service
public final class IntegerToJPLongArray extends BaseTypeParser<Integer, JPLongArray> {
  @Override
  public JPLongArray parse(Integer value) {
    if (value == null) {
      return null;
    }
    return JPLongArray.of(Collections.singletonList(parserService.parseTo(Long.class, value)));
  }

  @Override
  public Class<Integer> getInputType() {
    return Integer.class;
  }

  @Override
  public Class<JPLongArray> getOutputType() {
    return JPLongArray.class;
  }
}
