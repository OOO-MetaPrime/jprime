package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Date -> java.sql.Date
 */
@Service
public final class DateToSqlDateParser extends BaseTypeParser<Date, java.sql.Date> {
  @Override
  public java.sql.Date parse(Date value) {
    return value != null ? new java.sql.Date(value.getTime()) : null;
  }

  @Override
  public Class<Date> getInputType() {
    return Date.class;
  }

  @Override
  public Class<java.sql.Date> getOutputType() {
    return java.sql.Date.class;
  }
}
