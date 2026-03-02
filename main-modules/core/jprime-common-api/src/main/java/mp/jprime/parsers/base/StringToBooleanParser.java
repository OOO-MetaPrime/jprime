package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * String -> Boolean
 */
@Service
public final class StringToBooleanParser extends BaseTypeParser<String, Boolean> {
  @Override
  public Boolean parse(String value) {
    return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("t") ||
        value.equalsIgnoreCase("yes") || value.equals("1") || value.equalsIgnoreCase("on") ||
        value.equalsIgnoreCase("Д") || value.equalsIgnoreCase("Да") ||
        value.equalsIgnoreCase("Есть");
  }

  @Override
  public Class<String> getInputType() {
    return String.class;
  }

  @Override
  public Class<Boolean> getOutputType() {
    return Boolean.class;
  }
}
