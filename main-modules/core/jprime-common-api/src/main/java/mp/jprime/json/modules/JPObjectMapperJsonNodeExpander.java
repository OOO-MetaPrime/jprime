package mp.jprime.json.modules;

import mp.jprime.lang.JPJsonNode;
import org.springframework.stereotype.Service;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.cfg.MapperBuilder;
import tools.jackson.databind.deser.std.StdDeserializer;
import tools.jackson.databind.module.SimpleModule;


/**
 * Подключение базовых обработчиков
 */
@Service
public final class JPObjectMapperJsonNodeExpander implements JPObjectMapperExpander {

  @Override
  public void expand(MapperBuilder<?, ?> builder) {
    SimpleModule module = new SimpleModule()
        // JsonNode to JPJsonNode
        .addDeserializer(JPJsonNode.class, new StdDeserializer<>(JPJsonNode.class) {
          @Override
          public JPJsonNode deserialize(JsonParser p, DeserializationContext ctxt) {
            return JPJsonNode.from(p.readValueAsTree());
          }
        })
        // JPJsonNode to JsonNode
        .addSerializer(JPJsonNode.class,
            new ValueSerializer<>() {
              @Override
              public void serialize(JPJsonNode jpJsonNode, JsonGenerator jGen, SerializationContext sProv) {
                jGen.writePOJO(jpJsonNode.toJsonNode());
              }
            }
        );
    builder.addModule(module);
  }
}
