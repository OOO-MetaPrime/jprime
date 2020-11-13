package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * String -> Double
 */
@Service
public class StringToDoubleParser implements TypeParser<String, Double> {
  /**
   * Прекомпилированный шаблон замены
   */
  private static final Pattern pattern = Pattern.compile(" ", Pattern.LITERAL);

  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Double parse(String value) {
    if (value == null) {
      return null;
    }
    value = value.trim();
    if (value.isEmpty()) {
      return null;
    }
    value = value.replace(',', '.');
    return Double.valueOf(value.indexOf(' ') > -1 ? pattern.matcher(value).replaceAll("") : value);
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
  public Class<Double> getOutputType() {
    return Double.class;
  }
}
