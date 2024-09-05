package mp.jprime.parsers.base;

import mp.jprime.parsers.TypeParser;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Date -> java.sql.Date
 */
@Service
public final class DateToSqlDateParser implements TypeParser<Date, java.sql.Date> {
  /**
   * Форматирование значения
   *
   * @param value Данные во входном формате
   * @return Данные в выходном формате
   */
  public java.sql.Date parse(Date value) {
    return value != null ? new java.sql.Date(value.getTime()) : null;
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
  public Class<java.sql.Date> getOutputType() {
    return java.sql.Date.class;
  }
}
