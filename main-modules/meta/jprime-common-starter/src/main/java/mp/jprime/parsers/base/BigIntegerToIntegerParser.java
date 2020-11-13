package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * BigInteger -> Integer
 */
@Service
public class BigIntegerToIntegerParser implements TypeParser<BigInteger, Integer> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Integer parse(BigInteger value) {
    return value != null ? value.intValue() : null;
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<BigInteger> getInputType() {
    return BigInteger.class;
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