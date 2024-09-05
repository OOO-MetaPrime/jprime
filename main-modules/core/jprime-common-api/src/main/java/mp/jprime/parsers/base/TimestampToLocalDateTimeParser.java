package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Timestamp -> LocalDateTime
 */
@Service
public final class TimestampToLocalDateTimeParser implements TypeParser<Timestamp, LocalDateTime> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public LocalDateTime parse(Timestamp value) {
    return value != null ? value.toLocalDateTime() : null;
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<Timestamp> getInputType() {
    return Timestamp.class;
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
