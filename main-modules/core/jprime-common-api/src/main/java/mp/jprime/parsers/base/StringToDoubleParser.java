package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * String -> Double
 */
@Service
public final class StringToDoubleParser extends BaseTypeParser<String, Double> {
  private static final Pattern PATTERN = Pattern.compile(" ", Pattern.LITERAL);

  @Override
  public Double parse(String value) {
    if (value == null) {
      return null;
    }
    value = value.trim();
    if (value.isEmpty()) {
      return null;
    }
    value = value.replace(',', '.');
    return Double.valueOf(value.indexOf(' ') > -1 ? PATTERN.matcher(value).replaceAll("") : value);
  }

  @Override
  public Class<String> getInputType() {
    return String.class;
  }

  @Override
  public Class<Double> getOutputType() {
    return Double.class;
  }
}
