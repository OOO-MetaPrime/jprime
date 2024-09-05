package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * UUID -> String
 */
@Service
public final class UUIDToStringParser implements TypeParser<UUID, String> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public String parse(UUID value) {
    if (value == null) {
      return null;
    }
    return value.toString();
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<UUID> getInputType() {
    return UUID.class;
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
