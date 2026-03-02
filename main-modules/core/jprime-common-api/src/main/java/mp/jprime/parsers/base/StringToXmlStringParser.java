package mp.jprime.parsers.base;

import mp.jprime.lang.JPXmlString;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * String -> XmlString
 */
@Service
public final class StringToXmlStringParser extends BaseTypeParser<String, JPXmlString> {
  @Override
  public JPXmlString parse(String value) {
    return value == null || value.isEmpty() ? null : JPXmlString.from(value);
  }

  @Override
  public Class<String> getInputType() {
    return String.class;
  }

  @Override
  public Class<JPXmlString> getOutputType() {
    return JPXmlString.class;
  }
}
