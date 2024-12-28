package mp.jprime.parsers.base;

import mp.jprime.json.beans.JsonDateRange;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.lang.JPDateRange;
import mp.jprime.parsers.TypeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * {@link LinkedHashMap} -> {@link JPDateRange}
 */
@Service
public final class LinkedHashMapToJPDateRangeParser implements TypeParser<LinkedHashMap, JPDateRange> {
  private static final Logger LOG = LoggerFactory.getLogger(LinkedHashMapToJPDateRangeParser.class);
  private JPJsonMapper jsonMapper;

  @Autowired
  private void setJsonMapper(JPJsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  @Override
  public JPDateRange parse(LinkedHashMap value) {
    try {
      JsonDateRange json = jsonMapper.toObject(
          JsonDateRange.class,
          jsonMapper.toString(value)
      );
      return JPDateRange.create(
          json.getLower(),
          json.getUpper()
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
  public Class<JPDateRange> getOutputType() {
    return JPDateRange.class;
  }
}
