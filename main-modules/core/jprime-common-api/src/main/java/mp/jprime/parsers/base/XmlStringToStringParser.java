package mp.jprime.parsers.base;

import mp.jprime.lang.JPXmlString;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * XmlString -> String
 */
@Service
public final class XmlStringToStringParser implements TypeParser<JPXmlString, String> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public String parse(JPXmlString value) {
    return value == null ? null : value.toString();
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<JPXmlString> getInputType() {
    return JPXmlString.class;
  }

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  public Class<String> getOutputType() {
    return String.class;
  }
}
