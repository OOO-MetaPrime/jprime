package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * Long -> String
 */
@Service
public class LongToStringParser implements TypeParser<Long, String> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public String parse(Long value) {
    return value == null ? null : Long.toString(value);
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<Long> getInputType() {
    return Long.class;
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
