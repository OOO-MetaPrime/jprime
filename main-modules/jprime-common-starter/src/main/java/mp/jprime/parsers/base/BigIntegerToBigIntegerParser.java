package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * BigInteger -> BigInteger
 */
@Service
public class BigIntegerToBigIntegerParser implements TypeParser<BigInteger, BigInteger> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public BigInteger parse(BigInteger value) {
    return value;
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
  public Class<BigInteger> getOutputType() {
    return BigInteger.class;
  }
}