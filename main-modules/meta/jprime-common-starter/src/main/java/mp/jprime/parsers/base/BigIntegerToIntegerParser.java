package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import mp.jprime.parsers.exceptions.JPParseException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * BigInteger -> Integer
 */
@Service
public class BigIntegerToIntegerParser implements TypeParser<BigInteger, Integer> {
  public final static BigInteger MAX_INTEGER = BigInteger.valueOf(Integer.MAX_VALUE);
  public final static BigInteger MIN_INTEGER = BigInteger.valueOf(Integer.MIN_VALUE);

  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Integer parse(BigInteger value) {
    if (value == null) {
      return null;
    }
    if (value.compareTo(MIN_INTEGER) < 0 || value.compareTo(MAX_INTEGER) > 0) {
      throw new JPParseException("integer.parse", "Значение превышает размерность");
    }
    return value.intValue();
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