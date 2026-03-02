package mp.jprime.parsers.base;

import mp.jprime.json.beans.JsonSimpleFraction;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.lang.JPSimpleFraction;
import mp.jprime.parsers.BaseTypeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * {@link LinkedHashMap} -> {@link JPSimpleFraction}
 */
@Service
public final class LinkedHashMapToJPSimpleFractionParser extends BaseTypeParser<LinkedHashMap, JPSimpleFraction> {
  private static final Logger LOG = LoggerFactory.getLogger(LinkedHashMapToJPSimpleFractionParser.class);
  private JPJsonMapper jsonMapper;

  @Autowired
  private void setJsonMapper(JPJsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  @Override
  public JPSimpleFraction parse(LinkedHashMap value) {
    try {
      JsonSimpleFraction json = value == null ? null : jsonMapper.toObject(
          JsonSimpleFraction.class,
          jsonMapper.toString(value)
      );
      return json == null ? null : JPSimpleFraction.of(json.isPositive(), json.getInteger(), json.getNumerator(), json.getDenominator());
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
  public Class<JPSimpleFraction> getOutputType() {
    return JPSimpleFraction.class;
  }
}
