package mp.jprime.parsers.base;

import mp.jprime.lang.JPLongArray;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Long -> JPLongArray
 */
@Service
public final class LongToJPLongArray extends BaseTypeParser<Long, JPLongArray> {
  @Override
  public JPLongArray parse(Long value) {
    if (value == null) {
      return null;
    }
    return JPLongArray.of(Collections.singletonList(value));
  }

  @Override
  public Class<Long> getInputType() {
    return Long.class;
  }

  @Override
  public Class<JPLongArray> getOutputType() {
    return JPLongArray.class;
  }
}
