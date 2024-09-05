package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * Integer -> Double
 */
@Service
public final class IntegerToDoubleParser implements TypeParser<Integer, Double> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Double parse(Integer value) {
    return value != null ? value.doubleValue() : null;
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<Integer> getInputType() {
    return Integer.class;
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