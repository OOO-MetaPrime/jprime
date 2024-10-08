package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * Float -> Integer
 */
@Service
public final class FloatToIntegerParser implements TypeParser<Float, Integer> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Integer parse(Float value) {
    return value != null ? value.intValue() : null;
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
  public Class<Integer> getOutputType() {
    return Integer.class;
  }
}