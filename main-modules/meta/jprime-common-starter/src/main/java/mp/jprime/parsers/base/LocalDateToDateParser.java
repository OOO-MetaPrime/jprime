package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * LocalDate -> Date
 */
@Service
public class LocalDateToDateParser implements TypeParser<LocalDate, Date> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Date parse(LocalDate value) {
    return value == null ? null : Date.from(value.atStartOfDay(ZoneId.systemDefault()).toInstant());
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<LocalDate> getInputType() {
    return LocalDate.class;
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

