package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * Float -> Double
 */
@Service
public class FloatToDoubleParser implements TypeParser<Float, Double> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Double parse(Float value) {
    return value != null ? value.doubleValue() : null;
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
  public Class<Double> getOutputType() {
    return Double.class;
  }
}