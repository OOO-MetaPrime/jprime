package mp.jprime.parsers.base;

import mp.jprime.formats.DateFormat;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.TimeZone;

/**
 * String -> LocalDateTime
 */
@Service
public class StringToLocalDateTimeParser implements TypeParser<String, LocalDateTime> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public LocalDateTime parse(String value) {
    if (value == null || value.trim().isEmpty()) {
      return null;
    }
    ZonedDateTime zdt = ZonedDateTime.parse(value, DateFormat.LOCAL_DATETIME_FORMAT);
    return zdt == null ? null : zdt.withZoneSameInstant(TimeZone.getDefault().toZoneId()).toLocalDateTime();
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
  public Class<LocalDateTime> getOutputType() {
    return LocalDateTime.class;
  }
}
