package mp.jprime.parsers.base;

import mp.jprime.lang.JPLongArray;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * JPLongArray -> String
 */
@Service
public final class JPLongArrayToStringParser extends BaseTypeParser<JPLongArray, String> {

  @Override
  public String parse(JPLongArray value) {
    return value != null ? value.toList().toString() : null;
  }

  @Override
  public Class<JPLongArray> getInputType() {
    return JPLongArray.class;
  }

  @Override
  public Class<String> getOutputType() {
    return String.class;
  }
}
