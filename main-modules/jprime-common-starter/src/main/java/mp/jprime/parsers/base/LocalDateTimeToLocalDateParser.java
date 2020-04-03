package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * LocalDateTime -> LocalDate
 */
@Service
public class LocalDateTimeToLocalDateParser implements TypeParser<LocalDateTime, LocalDate> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public LocalDate parse(LocalDateTime value) {
    return value == null ? null : value.toLocalDate();
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
  public Class<LocalDate> getOutputType() {
    return LocalDate.class;
  }
}
