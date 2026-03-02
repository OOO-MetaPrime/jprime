package mp.jprime.parsers.base;

import mp.jprime.lang.JPStringArray;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * String[] -> JPStringArray
 */
@Service
public final class StringArrayToJPStringArray extends BaseTypeParser<String[], JPStringArray> {
  @Override
  public JPStringArray parse(String[] value) {
    return JPStringArray.of(value);
  }

  @Override
  public Class<String[]> getInputType() {
    return String[].class;
  }

  @Override
  public Class<JPStringArray> getOutputType() {
    return JPStringArray.class;
  }
}
