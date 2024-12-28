package mp.jprime.parsers.base;

import mp.jprime.json.beans.JsonDateTimeRange;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.lang.JPDateTimeRange;
import mp.jprime.parsers.TypeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * {@link LinkedHashMap} -> {@link JPDateTimeRange}
 */
@Service
public final class LinkedHashMapToJPDateTimeRangeParser implements TypeParser<LinkedHashMap, JPDateTimeRange> {
  private static final Logger LOG = LoggerFactory.getLogger(LinkedHashMapToJPDateTimeRangeParser.class);
  private JPJsonMapper jsonMapper;

  @Autowired
  private void setJsonMapper(JPJsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  @Override
  public JPDateTimeRange parse(LinkedHashMap value) {
    try {
      JsonDateTimeRange json = jsonMapper.toObject(
          JsonDateTimeRange.class,
          jsonMapper.toString(value)
      );
      return JPDateTimeRange.create(
          json.getLower(),
          json.getUpper(),
          json.isCloseLower(),
          json.isCloseUpper()
      );
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public Class<LinkedHashMap> getInputType() {
    return LinkedHashMap.class;
  }

  @Override
  public Class<JPDateTimeRange> getOutputType() {
    return JPDateTimeRange.class;
  }
}
