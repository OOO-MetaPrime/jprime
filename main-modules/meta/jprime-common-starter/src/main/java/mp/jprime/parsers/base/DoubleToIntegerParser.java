package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * Double -> Integer
 */
@Service
public class DoubleToIntegerParser implements TypeParser<Double, Integer> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Integer parse(Double value) {
    return value != null ? value.intValue() : null;
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
  public Class<Integer> getOutputType() {
    return Integer.class;
  }
}