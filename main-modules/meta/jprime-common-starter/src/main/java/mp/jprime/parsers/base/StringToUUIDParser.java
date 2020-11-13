package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * String -> UUID
 */
@Service
public class StringToUUIDParser implements TypeParser<String, UUID> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public UUID parse(String value) {
    if (value == null) {
      return null;
    }
    return UUID.fromString(value);
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
  public Class<UUID> getOutputType() {
    return UUID.class;
  }
}
