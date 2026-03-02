package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * java.sql.Date -> Date
 */
@Service
public final class SqlDateToDateParser extends BaseTypeParser<java.sql.Date, Date> {
  @Override
  public Date parse(java.sql.Date value) {
    return value != null ? new Date(value.getTime()) : null;
  }

  @Override
  public Class<java.sql.Date> getInputType() {
    return java.sql.Date.class;
  }

  @Override
  public Class<Date> getOutputType() {
    return Date.class;
  }
}