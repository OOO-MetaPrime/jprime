package mp.jprime.parsers.base;

import mp.jprime.formats.DateFormat;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * java.sql.Date -> String
 */
@Service
public final class SqlDateToStringParser extends BaseTypeParser<Date, String> {
  @Override
  public String parse(java.sql.Date value) {
    if (value == null) {
      return null;
    }
    LocalDate date = new Timestamp(value.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    return DateFormat.LOCAL_TIME_FORMAT.format(date);
  }

  @Override
  public Class<java.sql.Date> getInputType() {
    return java.sql.Date.class;
  }

  @Override
  public Class<String> getOutputType() {
    return String.class;
  }
}