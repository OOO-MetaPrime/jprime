package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * Boolean -> String
 */
@Service
public final class BooleanToStringParser extends BaseTypeParser<Boolean, String> {
  /**
   * Строковое значение, соответствующее ДА
   */
  private static final String TRUE = "true";
  /**
   * Строковое значение, соответствующее НЕТ
   */
  private static final String FALSE = "false";

  @Override
  public String parse(Boolean value) {
    return value != null && value ? TRUE : FALSE;
  }

  @Override
  public Class<Boolean> getInputType() {
    return Boolean.class;
  }

  @Override
  public Class<String> getOutputType() {
    return String.class;
  }
}
