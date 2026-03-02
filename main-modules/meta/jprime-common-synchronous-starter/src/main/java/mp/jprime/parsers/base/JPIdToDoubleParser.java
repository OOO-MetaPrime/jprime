package mp.jprime.parsers.base;

import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.parsers.BaseTypeParser;
import org.springframework.stereotype.Service;

/**
 * JPId -> Double
 */
@Service
public final class JPIdToDoubleParser extends BaseTypeParser<JPId, Double> {
  @Override
  public Double parse(JPId value) {
    return value == null ? null : parserService.parseTo(getOutputType(), value.getId());
  }

  @Override
  public Class<JPId> getInputType() {
    return JPId.class;
  }

  @Override
  public Class<Double> getOutputType() {
    return Double.class;
  }
}
