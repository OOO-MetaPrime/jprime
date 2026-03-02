package mp.jprime.parsers.base;

import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Timestamp -> Date
 */
@Service
public final class TimestampToDateParser extends BaseTypeParser<Timestamp, Date> {
  @Override
  public Date parse(Timestamp value) {
    return value != null ? new Date(value.getTime()) : null;
  }

  @Override
  public Class<Timestamp> getInputType() {
    return Timestamp.class;
  }

  @Override
  public Class<Date> getOutputType() {
    return Date.class;
  }
}
