package mp.jprime.parsers.base;

import mp.jprime.json.format.DateTimeFormat;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.TimeZone;

/**
 * LocalDateTime -> String
 */
@Service
public class LocalDateTimeToStringParser implements TypeParser<LocalDateTime, String> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public String parse(LocalDateTime value) {
    if (value == null) {
      return null;
    }
    return DateTimeFormat.LOCAL_DATETIME_FORMAT.format(ZonedDateTime.of(value, TimeZone.getDefault().toZoneId()));
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<LocalDateTime> getInputType() {
    return LocalDateTime.class;
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
