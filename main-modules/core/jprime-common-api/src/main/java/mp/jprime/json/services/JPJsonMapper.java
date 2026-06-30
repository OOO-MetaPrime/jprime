package mp.jprime.json.services;

import mp.jprime.json.modules.JPObjectMapperExpander;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.cfg.JsonNodeFeature;
import tools.jackson.databind.json.JsonMapper;

import java.util.Collection;

/**
 * Базовый класс JSON-обработчиков
 */
@Service
public class JPJsonMapper extends JPObjectMapper {
  private static JsonMapper OBJECT_MAPPER;

  private JPJsonMapper(@Autowired Collection<JPObjectMapperExpander> expanders) {
    JsonMapper.Builder builder = JsonMapper.builder()
        // 1. Tell Jackson not to normalize/strip trailing zeros in the Tree Model (JsonNode)
        .disable(JsonNodeFeature.STRIP_TRAILING_BIGDECIMAL_ZEROES)
        // 2. Optional: Ensure floating point numbers parse as BigDecimal instead of Double
        .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
        .disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);

    setSettings(expanders, builder);

    OBJECT_MAPPER = builder.build();
  }

  @Override
  public JsonMapper getObjectMapper() {
    return OBJECT_MAPPER;
  }

  /**
   * ObjectMapper
   *
   * @return ObjectMapper
   */
  public static ObjectMapper getMapper() {
    return OBJECT_MAPPER;
  }
}
