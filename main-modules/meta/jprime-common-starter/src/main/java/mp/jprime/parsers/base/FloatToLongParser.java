package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * Float -> Long
 */
@Service
public class FloatToLongParser implements TypeParser<Float, Long> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Long parse(Float value) {
    return value != null ? value.longValue() : null;
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
  public Class<Long> getOutputType() {
    return Long.class;
  }
}