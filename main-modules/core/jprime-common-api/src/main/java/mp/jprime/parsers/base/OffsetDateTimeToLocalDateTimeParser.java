package mp.jprime.parsers.base;


import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * OffsetDateTime -> LocalDateTime
 */
@Service
public final class OffsetDateTimeToLocalDateTimeParser implements TypeParser<OffsetDateTime, LocalDateTime> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public LocalDateTime parse(OffsetDateTime value) {
    return value != null ? value.toLocalDateTime() : null;
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<OffsetDateTime> getInputType() {
    return OffsetDateTime.class;
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
