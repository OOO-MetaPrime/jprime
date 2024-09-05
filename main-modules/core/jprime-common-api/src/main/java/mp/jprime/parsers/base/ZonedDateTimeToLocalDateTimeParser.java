package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.TimeZone;

/**
 * ZonedDateTime -> LocalDateTime
 */
@Service
public final class ZonedDateTimeToLocalDateTimeParser implements TypeParser<ZonedDateTime, LocalDateTime> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public LocalDateTime parse(ZonedDateTime value) {
    return value == null ? null : value.withZoneSameInstant(TimeZone.getDefault().toZoneId()).toLocalDateTime();
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<ZonedDateTime> getInputType() {
    return ZonedDateTime.class;
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
