package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * String -> Float
 */
@Service
public class StringToFloatParser implements TypeParser<String, Float> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Float parse(String value) {
    return value == null || value.isEmpty() ? null : Float.parseFloat(value.trim());
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
  public Class<Float> getOutputType() {
    return Float.class;
  }
}
