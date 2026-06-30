package mp.jprime.json.modules;

import mp.jprime.parsers.ValueParser;
import mp.jprime.xml.modules.JPObjectMapperXmlExpander;
import org.springframework.stereotype.Service;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.KeyDeserializer;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.cfg.MapperBuilder;
import tools.jackson.databind.deser.std.StdDeserializer;
import tools.jackson.databind.module.SimpleModule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Подключение JavaTime
 */
@Service
public final class JPObjectMapperJavaTimeExpander implements JPObjectMapperExpander, JPObjectMapperXmlExpander {

  private LocalTime toLocalTime(String value) {
    value = value != null ? value.trim() : null;
    if (value == null || value.isEmpty()) {
      return null;
    }
    try {
      return ValueParser.parseTo(LocalTime.class, value);
    } catch (Exception e) {
      return null;
    }
  }

  private LocalDate toLocalDate(String value) {
    value = value != null ? value.trim() : null;
    if (value == null || value.isEmpty()) {
      return null;
    }
    try {
      return ValueParser.parseTo(LocalDate.class, value);
    } catch (Exception e) {
      return null;
    }
  }

  private LocalDateTime toLocalDateTime(String value) {
    value = value != null ? value.trim() : null;
    if (value == null || value.isEmpty()) {
      return null;
    }
    try {
      return ValueParser.parseTo(LocalDateTime.class, value);
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public void expand(MapperBuilder<?, ?> builder) {
    SimpleModule module = new SimpleModule()
        // String to LocalTime
        .addKeyDeserializer(LocalTime.class, new KeyDeserializer() {
          @Override
          public LocalTime deserializeKey(String value, DeserializationContext ctxt) {
            return toLocalTime(value);
          }
        })
        .addDeserializer(LocalTime.class, new StdDeserializer<>(LocalTime.class) {
          @Override
          public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) {
            return toLocalTime(p.getString());
          }
        })
        // String to LocalDateTime
        .addKeyDeserializer(LocalDateTime.class, new KeyDeserializer() {
          @Override
          public LocalDateTime deserializeKey(String value, DeserializationContext ctxt) {
            return toLocalDateTime(value);
          }
        })
        .addDeserializer(LocalDateTime.class, new StdDeserializer<>(LocalDateTime.class) {
          @Override
          public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) {
            return toLocalDateTime(p.getString());
          }
        })
        // String to LocalDate
        .addKeyDeserializer(LocalDate.class, new KeyDeserializer() {
          @Override
          public LocalDate deserializeKey(String value, DeserializationContext ctxt) {
            return toLocalDate(value);
          }
        })
        .addDeserializer(LocalDate.class, new StdDeserializer<>(LocalDate.class) {
          @Override
          public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) {
            return toLocalDate(p.getString());
          }
        })
        // LocalTime to String
        .addSerializer(LocalTime.class,
            new ValueSerializer<>() {
              @Override
              public void serialize(LocalTime v, JsonGenerator jGen, SerializationContext sProv) {
                jGen.writeString(ValueParser.parseTo(String.class, v));
              }
            }
        )
        // LocalDateTime to String
        .addSerializer(LocalDateTime.class,
            new ValueSerializer<>() {
              @Override
              public void serialize(LocalDateTime v, JsonGenerator jGen, SerializationContext sProv) {
                jGen.writeString(ValueParser.parseTo(String.class, v));
              }
            }
        )
        // LocalDate to String
        .addSerializer(LocalDate.class,
            new ValueSerializer<>() {
              @Override
              public void serialize(LocalDate v, JsonGenerator jGen, SerializationContext sProv) {
                jGen.writeString(ValueParser.parseTo(String.class, v));
              }
            }
        );

    builder.addModule(module);
  }
}
