package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * String -> BigDecimal
 */
@Service
public final class StringToBigDecimalParser extends BaseTypeParser<String, BigDecimal> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   *
   * @return Данные в выходном формате
   */
  public BigDecimal parse(String value) {
    if (StringUtils.isBlank(value)) {
      return null;
    }
    char[] result = new char[value.length()];
    int index = 0;

    for (char c : value.toCharArray()) {
      if (c == ',') {
        result[index++] = '.';
      } else if (!Character.isWhitespace(c) && c != '\u00A0') {
        result[index++] = (c);
      }
    }

    return new BigDecimal(new String(result, 0, index));
  }

  @Override
  public Class<String> getInputType() {
    return String.class;
  }

  @Override
  public Class<BigDecimal> getOutputType() {
    return BigDecimal.class;
  }
}
