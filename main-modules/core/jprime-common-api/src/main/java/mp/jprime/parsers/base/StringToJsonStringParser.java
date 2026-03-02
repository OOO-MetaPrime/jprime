package mp.jprime.parsers.base;


import mp.jprime.lang.JPJsonString;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * String -> JsonString
 */
@Service
public final class StringToJsonStringParser extends BaseTypeParser<String, JPJsonString> {
  @Override
  public JPJsonString parse(String value) {
    return value == null || value.isEmpty() ? null : JPJsonString.from(value);
  }

  @Override
  public Class<String> getInputType() {
    return String.class;
  }

  @Override
  public Class<JPJsonString> getOutputType() {
    return JPJsonString.class;
  }
}
