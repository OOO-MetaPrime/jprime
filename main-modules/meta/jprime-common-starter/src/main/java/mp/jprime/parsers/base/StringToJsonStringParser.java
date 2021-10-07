package mp.jprime.parsers.base;


import mp.jprime.lang.JPJsonString;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * String -> JsonString
 */
@Service
public class StringToJsonStringParser implements TypeParser<String, JPJsonString> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public JPJsonString parse(String value) {
    return value == null || value.isEmpty() ? null : JPJsonString.from(value);
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
  public Class<JPJsonString> getOutputType() {
    return JPJsonString.class;
  }
}
