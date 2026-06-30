package mp.jprime.parsers.base;

import mp.jprime.lang.JPStringArray;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * JPStringArray -> String
 */
@Service
public final class JPStringArrayToStringParser extends BaseTypeParser<JPStringArray, String> {

  @Override
  public String parse(JPStringArray value) {
    return value != null ? value.toList().toString() : null;
  }

  @Override
  public Class<JPStringArray> getInputType() {
    return JPStringArray.class;
  }

  @Override
  public Class<String> getOutputType() {
    return String.class;
  }
}
