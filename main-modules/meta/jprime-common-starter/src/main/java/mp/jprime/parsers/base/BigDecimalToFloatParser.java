package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import mp.jprime.parsers.exceptions.JPParseException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * BigDecimal -> Float
 */
@Service
public class BigDecimalToFloatParser implements TypeParser<BigDecimal, Float> {
  public final static BigDecimal MAX_FLOAT = BigDecimal.valueOf(Float.MAX_VALUE);
  public final static BigDecimal MIN_FLOAT = BigDecimal.valueOf(-1 * Float.MAX_VALUE);

  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Float parse(BigDecimal value) {
    if (value == null) {
      return null;
    }
    if (value.compareTo(MIN_FLOAT) < 0 || value.compareTo(MAX_FLOAT) > 0) {
      throw new JPParseException("float.parse", "Значение превышает размерность");
    }
    return value.floatValue();
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<BigDecimal> getInputType() {
    return BigDecimal.class;
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