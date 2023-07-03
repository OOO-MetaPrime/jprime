package mp.jprime.json.modules;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import mp.jprime.lang.JPJsonNode;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Подключение базовых обработчиков
 */
@Service
public final class JPObjectMapperJsonNodeExpander implements JPObjectMapperExpander {

  @Override
  public void expand(ObjectMapper objectMapper) {
    SimpleModule module = new SimpleModule()
        // JsonNode to JPJsonNode
        .addDeserializer(JPJsonNode.class, new StdDeserializer<JPJsonNode>(JPJsonNode.class) {
          @Override
          public JPJsonNode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return JPJsonNode.from(p.getCodec().readTree(p));
          }
        })
        // JPJsonNode to JsonNode
        .addSerializer(JPJsonNode.class,
            new JsonSerializer<JPJsonNode>() {
              @Override
              public void serialize(JPJsonNode jpJsonNode, JsonGenerator jGen, SerializerProvider sProv) throws IOException {
                jGen.writeObject(jpJsonNode.toJsonNode());
              }
            }
        );
    objectMapper.registerModule(module);
  }
}
