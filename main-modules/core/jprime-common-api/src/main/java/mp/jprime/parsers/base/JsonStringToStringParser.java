package mp.jprime.parsers.base;

import mp.jprime.lang.JPJsonString;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * JsonString -> String
 */
@Service
public final class JsonStringToStringParser extends BaseTypeParser<JPJsonString, String> {
  @Override
  public String parse(JPJsonString value) {
    return value == null ? null : value.toString();
  }

  @Override
  public Class<JPJsonString> getInputType() {
    return JPJsonString.class;
  }

  @Override
  public Class<String> getOutputType() {
    return String.class;
  }
}
