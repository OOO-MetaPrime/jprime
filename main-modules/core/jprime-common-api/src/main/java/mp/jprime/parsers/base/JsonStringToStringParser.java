package mp.jprime.parsers.base;

import mp.jprime.lang.JPJsonString;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * JsonString -> String
 */
@Service
public final class JsonStringToStringParser implements TypeParser<JPJsonString, String> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public String parse(JPJsonString value) {
    return value == null ? null : value.toString();
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<JPJsonString> getInputType() {
    return JPJsonString.class;
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
