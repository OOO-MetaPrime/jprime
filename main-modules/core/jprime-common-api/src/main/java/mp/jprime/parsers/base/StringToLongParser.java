package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * String -> Long
 */
@Service
public final class StringToLongParser implements TypeParser<String, Long> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Long parse(String value) {
    return value == null || value.isEmpty() ? null : Long.parseLong(value.trim());
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
  public Class<Long> getOutputType() {
    return Long.class;
  }
}
