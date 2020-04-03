package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

/**
 * Boolean -> String
 */
@Service
public class BooleanToStringParser implements TypeParser<Boolean, String> {
  /**
   * Строкое значение, соответствующее ДА
   */
  private static final String TRUE = "true";
  /**
   * Строкое значение, соответствующее НЕТ
   */
  private static final String FALSE = "false";

  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public String parse(Boolean value) {
    return value != null && value ? TRUE : FALSE;
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<Boolean> getInputType() {
    return Boolean.class;
  }

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  public Class<String> getOutputType() {
    return String.class;
  }
}
