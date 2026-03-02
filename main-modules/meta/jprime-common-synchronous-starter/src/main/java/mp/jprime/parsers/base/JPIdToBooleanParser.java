package mp.jprime.parsers.base;

import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * JPId -> Boolean
 */
@Service
public final class JPIdToBooleanParser extends BaseTypeParser<JPId, Boolean> {
  @Override
  public Boolean parse(JPId value) {
    return value == null ? null : parserService.parseTo(getOutputType(), value.getId());
  }

  @Override
  public Class<JPId> getInputType() {
    return JPId.class;
  }

  @Override
  public Class<Boolean> getOutputType() {
    return Boolean.class;
  }
}
