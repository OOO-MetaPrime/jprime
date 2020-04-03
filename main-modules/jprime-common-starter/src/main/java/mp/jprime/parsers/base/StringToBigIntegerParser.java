package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * String -> BigInteger
 */
@Service
public class StringToBigIntegerParser implements TypeParser<String, BigInteger> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public BigInteger parse(String value) {
    return value == null || value.isEmpty() ? null : new BigInteger(value.trim());
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<String> getInputType() {
    return String.class;
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
