package mp.jprime.parsers.base;

import mp.jprime.lang.XmlString;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * XmlString -> String
 */
@Service
public class XmlStringToStringParser implements TypeParser<XmlString, String> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public String parse(XmlString value) {
    return value == null ? null : value.toString();
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<XmlString> getInputType() {
    return XmlString.class;
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
