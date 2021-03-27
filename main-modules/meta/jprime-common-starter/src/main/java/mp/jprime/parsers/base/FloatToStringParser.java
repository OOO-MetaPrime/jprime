package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * Float -> String
 */
@Service
public class FloatToStringParser implements TypeParser<Float, String> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public String parse(Float value) {
    return value != null ? Float.toString(value) : null;
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<Float> getInputType() {
    return Float.class;
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