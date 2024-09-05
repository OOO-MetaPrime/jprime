package mp.jprime.parsers.base;

import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

import static mp.jprime.formats.DateFormat.ISO8601;

/**
 * String -> Date
 */
@Service
public final class StringToDateParser implements TypeParser<String, Date> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Date parse(String value) {
    try {
      return value == null || value.isEmpty() ? null : new SimpleDateFormat(ISO8601).parse(value.trim());
    } catch (Exception e) {
      throw JPRuntimeException.wrapException(e.getMessage(), e, JPRuntimeException.class);
    }
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
  public Class<Date> getOutputType() {
    return Date.class;
  }
}
