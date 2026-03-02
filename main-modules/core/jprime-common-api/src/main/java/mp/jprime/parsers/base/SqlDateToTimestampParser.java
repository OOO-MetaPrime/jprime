package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * java.sql.Date -> Timestamp
 */
@Service
public final class SqlDateToTimestampParser extends BaseTypeParser<Date, Timestamp> {
  @Override
  public Timestamp parse(Date value) {
    return value != null ? new Timestamp(value.getTime()) : null;
  }

  @Override
  public Class<Date> getInputType() {
    return Date.class;
  }

  @Override
  public Class<Timestamp> getOutputType() {
    return Timestamp.class;
  }
}
