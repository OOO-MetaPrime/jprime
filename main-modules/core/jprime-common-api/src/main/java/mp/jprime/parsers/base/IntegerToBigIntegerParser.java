package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * Integer -> BigInteger
 */
@Service
public final class IntegerToBigIntegerParser implements TypeParser<Integer, BigInteger> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public BigInteger parse(Integer value) {
    return value != null ? new BigInteger(value.toString()) : null;
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
  public Class<BigInteger> getOutputType() {
    return BigInteger.class;
  }
}