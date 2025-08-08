package mp.jprime.parsers.base;

import mp.jprime.formats.DateFormat;
import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * java.sql.Date -> String
 */
@Service
public final class SqlDateToStringParser implements TypeParser<java.sql.Date, String> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public String parse(java.sql.Date value) {
    if (value == null) {
      return null;
    }
    LocalDate date = new Timestamp(value.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    return DateFormat.LOCAL_TIME_FORMAT.format(date);
  }

  /**
   * Входной формат
   *
   * @return Входной формат
   */
  public Class<java.sql.Date> getInputType() {
    return java.sql.Date.class;
  }

  /**
   * Выходной формат
   *
   * @return Входной формат
   */
  public Class<String> getOutputType() {
    return String.class;
  }
}