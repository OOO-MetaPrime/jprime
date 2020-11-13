package mp.jprime.parsers.base;

import mp.jprime.lang.JsonString;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * JsonString -> String
 */
@Service
public class JsonStringToStringParser implements TypeParser<JsonString, String> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public String parse(JsonString value) {
    return value == null ? null : value.toString();
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<JsonString> getInputType() {
    return JsonString.class;
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
