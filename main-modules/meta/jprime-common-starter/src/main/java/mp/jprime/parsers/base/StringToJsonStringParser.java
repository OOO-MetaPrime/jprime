package mp.jprime.parsers.base;


import mp.jprime.lang.JsonString;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * String -> JsonString
 */
@Service
public class StringToJsonStringParser implements TypeParser<String, JsonString> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public JsonString parse(String value) {
    return value == null || value.isEmpty() ? null : JsonString.from(value);
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
  public Class<JsonString> getOutputType() {
    return JsonString.class;
  }
}
