package mp.jprime.xml.modules;

import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.lang.JPJsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.*;
import tools.jackson.databind.cfg.MapperBuilder;
import tools.jackson.databind.deser.std.StdDeserializer;
import tools.jackson.databind.module.SimpleModule;


/**
 * Подключение базовых обработчиков
 */
@Service
public final class JPObjectMapperXmlJsonNodeExpander implements JPObjectMapperXmlExpander {
  private static final Logger LOG = LoggerFactory.getLogger(JPObjectMapperXmlJsonNodeExpander.class);
  private JPJsonMapper jsonMapper;

  @Autowired
  private void setJsonMapper(JPJsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
  }

  @Override
  public void expand(MapperBuilder<?, ?> builder) {
    SimpleModule module = new SimpleModule()
        // String to JPJsonNode
        .addDeserializer(JPJsonNode.class, new StdDeserializer<>(JPJsonNode.class) {
          @Override
          public JPJsonNode deserialize(JsonParser p, DeserializationContext ctxt)  {
            JsonNode jsonNode = null;
            String value = p.getValueAsString();
            try {
              jsonNode = !StringUtils.hasText(value) ? null :
                  jsonMapper.getObjectMapper().readTree(value);
            } catch (JacksonException e) {
              LOG.error(e.getMessage(), e);
            }
            return JPJsonNode.from(jsonNode);
          }
        })
        // JPJsonNode to String
        .addSerializer(JPJsonNode.class,
            new ValueSerializer<>() {
              @Override
              public void serialize(JPJsonNode jpJsonNode, JsonGenerator jGen, SerializationContext sProv) {
                jGen.writePOJO(jpJsonNode.toJsonNode().toString());
              }
            }
        );
    builder.addModule(module);
  }
}
