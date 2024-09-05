package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * Long -> Double
 */
@Service
public final class LongToDoubleParser implements TypeParser<Long, Double> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Double parse(Long value) {
    return value != null ? value.doubleValue() : null;
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
  public Class<Double> getOutputType() {
    return Double.class;
  }
}