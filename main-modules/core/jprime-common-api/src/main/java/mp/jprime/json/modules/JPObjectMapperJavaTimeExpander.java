package mp.jprime.json.modules;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import mp.jprime.parsers.ValueParser;
import mp.jprime.xml.modules.JPObjectMapperXmlExpander;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
  public void expand(ObjectMapper objectMapper) {
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
          public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return toLocalTime(p.getText());
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
          public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return toLocalDateTime(p.getText());
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
          public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return toLocalDate(p.getText());
          }
        })
        // LocalTime to String
        .addSerializer(LocalTime.class,
            new JsonSerializer<>() {
              @Override
              public void serialize(LocalTime v, JsonGenerator jGen, SerializerProvider sProv) throws IOException {
                jGen.writeString(ValueParser.parseTo(String.class, v));
              }
            }
        )
        // LocalDateTime to String
        .addSerializer(LocalDateTime.class,
            new JsonSerializer<>() {
              @Override
              public void serialize(LocalDateTime v, JsonGenerator jGen, SerializerProvider sProv) throws IOException {
                jGen.writeString(ValueParser.parseTo(String.class, v));
              }
            }
        )
        // LocalDate to String
        .addSerializer(LocalDate.class,
            new JsonSerializer<>() {
              @Override
              public void serialize(LocalDate v, JsonGenerator jGen, SerializerProvider sProv) throws IOException {
                jGen.writeString(ValueParser.parseTo(String.class, v));
              }
            }
        );

    objectMapper.registerModule(module);
  }
}
