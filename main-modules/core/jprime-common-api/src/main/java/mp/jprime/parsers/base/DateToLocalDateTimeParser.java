package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Date -> LocalDate
 */
@Service
public final class DateToLocalDateTimeParser implements TypeParser<Date, LocalDateTime> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public LocalDateTime parse(Date value) {
    return value != null ? value.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(): null;
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
