package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import mp.jprime.parsers.exceptions.JPParseException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * BigDecimal -> Long
 */
@Service
public final class BigDecimalToLongParser implements TypeParser<BigDecimal, Long> {
  public final static BigDecimal MAX_LONG = BigDecimal.valueOf(Long.MAX_VALUE);
  public final static BigDecimal MIN_LONG = BigDecimal.valueOf(Long.MIN_VALUE);
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Long parse(BigDecimal value) {
    if (value == null) {
      return null;
    }
    if (value.compareTo(MIN_LONG) < 0 || value.compareTo(MAX_LONG) > 0) {
      throw new JPParseException("long.parse", "Значение превышает размерность");
    }
    return value.longValue();
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
  public Class<Long> getOutputType() {
    return Long.class;
  }
}

