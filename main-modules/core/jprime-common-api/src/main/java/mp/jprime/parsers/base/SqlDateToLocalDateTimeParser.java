package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * java.sql.Date -> LocalDate
 */
@Service
public final class SqlDateToLocalDateTimeParser implements TypeParser<Date, LocalDateTime> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public LocalDateTime parse(Date value) {
    return value != null ? new Timestamp(value.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() : null;
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<Date> getInputType() {
    return Date.class;
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
