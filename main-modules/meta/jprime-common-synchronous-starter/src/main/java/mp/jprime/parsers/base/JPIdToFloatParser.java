package mp.jprime.parsers.base;

import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * JPId -> Float
 */
@Service
public final class JPIdToFloatParser extends BaseTypeParser<JPId, Float> {
  @Override
  public Float parse(JPId value) {
    return value == null ? null : parserService.parseTo(getOutputType(), value.getId());
  }

  @Override
  public Class<JPId> getInputType() {
    return JPId.class;
  }

  @Override
  public Class<Float> getOutputType() {
    return Float.class;
  }
}
