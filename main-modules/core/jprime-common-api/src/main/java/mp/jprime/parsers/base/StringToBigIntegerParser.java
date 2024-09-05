package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * String -> BigInteger
 */
@Service
public final class StringToBigIntegerParser implements TypeParser<String, BigInteger> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   *
   * @return Данные в выходном формате
   */
  public BigInteger parse(String value) {
    if (StringUtils.isBlank(value)) {
      return null;
    }
    char[] result = new char[value.length()];
    int index = 0;

    for (char c : value.toCharArray()) {
      if (!Character.isWhitespace(c) && c != '\u00A0') {
        result[index++] = (c);
      }
    }

    return new BigInteger(new String(result, 0, index));
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
