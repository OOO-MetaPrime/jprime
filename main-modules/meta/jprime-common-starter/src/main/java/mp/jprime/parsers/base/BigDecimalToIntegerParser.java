package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * BigDecimal -> Integer
 */
@Service
public class BigDecimalToIntegerParser implements TypeParser<BigDecimal, Integer> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Integer parse(BigDecimal value) {
    return value != null ? value.intValue() : null;
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
  public Class<Integer> getOutputType() {
    return Integer.class;
  }
}