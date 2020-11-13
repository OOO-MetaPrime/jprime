package mp.jprime.parsers.base;

import mp.jprime.lang.XmlString;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * String -> XmlString
 */
@Service
public class StringToXmlStringParser implements TypeParser<String, XmlString> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public XmlString parse(String value) {
    return value == null || value.isEmpty() ? null : XmlString.from(value);
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<String> getInputType() {
    return String.class;
  }

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  public Class<XmlString> getOutputType() {
    return XmlString.class;
  }
}
