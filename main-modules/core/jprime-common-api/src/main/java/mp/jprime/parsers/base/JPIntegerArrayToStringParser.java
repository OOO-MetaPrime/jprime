package mp.jprime.parsers.base;

import mp.jprime.lang.JPIntegerArray;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * JPIntegerArray -> String
 */
@Service
public final class JPIntegerArrayToStringParser extends BaseTypeParser<JPIntegerArray, String> {

  @Override
  public String parse(JPIntegerArray value) {
    return value != null ? value.toList().toString() : null;
  }

  @Override
  public Class<JPIntegerArray> getInputType() {
    return JPIntegerArray.class;
  }

  @Override
  public Class<String> getOutputType() {
    return String.class;
  }
}
