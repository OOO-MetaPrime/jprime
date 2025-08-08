package mp.jprime.xml.modules;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.lang.JPJsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;

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
  public void expand(ObjectMapper objectMapper) {
    SimpleModule module = new SimpleModule()
        // String to JPJsonNode
        .addDeserializer(JPJsonNode.class, new StdDeserializer<>(JPJsonNode.class) {
          @Override
          public JPJsonNode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode jsonNode = null;
            String value = p.getValueAsString();
            try {
              jsonNode = !StringUtils.hasText(value) ? null :
                  jsonMapper.getObjectMapper().readTree(value);
            } catch (JsonProcessingException e) {
              LOG.error(e.getMessage(), e);
            }
            return JPJsonNode.from(jsonNode);
          }
        })
        // JPJsonNode to String
        .addSerializer(JPJsonNode.class,
            new JsonSerializer<>() {
              @Override
              public void serialize(JPJsonNode jpJsonNode, JsonGenerator jGen, SerializerProvider sProv) throws IOException {
                jGen.writeObject(jpJsonNode.toJsonNode().toString());
              }
            }
        );
    objectMapper.registerModule(module);
  }
}
