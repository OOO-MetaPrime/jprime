package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * LocalDate -> LocalDateTime
 */
@Service
public class LocalDateToLocalDateTimeParser implements TypeParser<LocalDate, LocalDateTime> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public LocalDateTime parse(LocalDate value) {
    return value == null ? null : value.atStartOfDay();
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
  public Class<LocalDateTime> getOutputType() {
    return LocalDateTime.class;
  }
}