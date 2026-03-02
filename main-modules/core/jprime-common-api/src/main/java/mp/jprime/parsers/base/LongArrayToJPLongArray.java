package mp.jprime.parsers.base;

import mp.jprime.lang.JPLongArray;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * Long[] -> JPLongArray
 */
@Service
public final class LongArrayToJPLongArray extends BaseTypeParser<Long[], JPLongArray> {
  @Override
  public JPLongArray parse(Long[] value) {
    return JPLongArray.of(value);
  }

  @Override
  public Class<Long[]> getInputType() {
    return Long[].class;
  }

  @Override
  public Class<JPLongArray> getOutputType() {
    return JPLongArray.class;
  }
}
