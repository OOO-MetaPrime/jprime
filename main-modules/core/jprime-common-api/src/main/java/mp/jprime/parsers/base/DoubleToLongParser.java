package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * Double -> Long
 */
@Service
public final class DoubleToLongParser implements TypeParser<Double, Long> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Long parse(Double value) {
    return value != null ? value.longValue() : null;
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
  public Class<Long> getOutputType() {
    return Long.class;
  }
}