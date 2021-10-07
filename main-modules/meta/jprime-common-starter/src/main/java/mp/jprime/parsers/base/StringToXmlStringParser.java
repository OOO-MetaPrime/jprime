package mp.jprime.parsers.base;

import mp.jprime.lang.JPXmlString;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * String -> XmlString
 */
@Service
public class StringToXmlStringParser implements TypeParser<String, JPXmlString> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public JPXmlString parse(String value) {
    return value == null || value.isEmpty() ? null : JPXmlString.from(value);
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
  public Class<JPXmlString> getOutputType() {
    return JPXmlString.class;
  }
}
