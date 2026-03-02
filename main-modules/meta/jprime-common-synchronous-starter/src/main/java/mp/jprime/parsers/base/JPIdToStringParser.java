package mp.jprime.parsers.base;

import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * JPId -> String
 */
@Service
public final class JPIdToStringParser extends BaseTypeParser<JPId, String> {
  @Override
  public String parse(JPId value) {
    return value == null ? null : parserService.parseTo(getOutputType(), value.getId());
  }

  @Override
  public Class<JPId> getInputType() {
    return JPId.class;
  }

  @Override
  public Class<String> getOutputType() {
    return String.class;
  }
}
