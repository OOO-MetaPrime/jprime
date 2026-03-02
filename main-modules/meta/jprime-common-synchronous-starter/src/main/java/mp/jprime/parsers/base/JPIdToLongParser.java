package mp.jprime.parsers.base;

import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * JPId -> Long
 */
@Service
public final class JPIdToLongParser extends BaseTypeParser<JPId, Long> {
  @Override
  public Long parse(JPId value) {
    return value == null ? null : parserService.parseTo(getOutputType(), value.getId());
  }

  @Override
  public Class<JPId> getInputType() {
    return JPId.class;
  }

  @Override
  public Class<Long> getOutputType() {
    return Long.class;
  }
}
