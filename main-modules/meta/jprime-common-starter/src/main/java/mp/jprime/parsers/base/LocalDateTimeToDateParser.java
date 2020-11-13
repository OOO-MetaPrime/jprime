package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * LocalDateTime -> Date
 */
@Service
public class LocalDateTimeToDateParser implements TypeParser<LocalDateTime, Date> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Date parse(LocalDateTime value) {
    return value == null ? null : Date.from(value.atZone(ZoneId.systemDefault()).toInstant());
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
  public Class<Date> getOutputType() {
    return Date.class;
  }
}

