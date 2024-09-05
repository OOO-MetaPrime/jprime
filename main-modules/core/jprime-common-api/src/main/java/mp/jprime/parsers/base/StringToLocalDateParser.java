package mp.jprime.parsers.base;

import mp.jprime.formats.DateFormat;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * String -> LocalDate
 */
@Service
public final class StringToLocalDateParser implements TypeParser<String, LocalDate> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public LocalDate parse(String value) {
    if (value == null || value.length() < 10) {
      return null;
    }
    if (value.length() > 10) {
      value = value.substring(0, 10);
    }
    return LocalDate.parse(value, DateFormat.LOCAL_DATE_FORMAT);
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<String> getInputType() {
    return String.class;
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
