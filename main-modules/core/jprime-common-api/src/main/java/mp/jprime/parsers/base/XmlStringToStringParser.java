package mp.jprime.parsers.base;

import mp.jprime.lang.JPXmlString;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * XmlString -> String
 */
@Service
public final class XmlStringToStringParser extends BaseTypeParser<JPXmlString, String> {
  @Override
  public String parse(JPXmlString value) {
    return value == null ? null : value.toString();
  }

  @Override
  public Class<JPXmlString> getInputType() {
    return JPXmlString.class;
  }

  @Override
  public Class<String> getOutputType() {
    return String.class;
  }
}
