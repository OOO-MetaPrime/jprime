package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Date -> LocalDate
 */
@Service
public final class DateToLocalDateParser implements TypeParser<Date, LocalDate> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public LocalDate parse(Date value) {
    return value != null ? value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(): null;
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
  public Class<LocalDate> getOutputType() {
    return LocalDate.class;
  }
}
