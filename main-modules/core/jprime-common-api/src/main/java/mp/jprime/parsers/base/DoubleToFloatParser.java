package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * Double -> Float
 */
@Service
public final class DoubleToFloatParser implements TypeParser<Double, Float> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Float parse(Double value) {
    return value != null ? value.floatValue() : null;
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<Double> getInputType() {
    return Double.class;
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