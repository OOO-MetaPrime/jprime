package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * java.sql.Date -> Date
 */
@Service
public class SqlDateToDateParser implements TypeParser<java.sql.Date, Date> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public Date parse(java.sql.Date value) {
    return value != null ? new Date(value.getTime()) : null;
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
  public Class<Date> getOutputType() {
    return Date.class;
  }
}