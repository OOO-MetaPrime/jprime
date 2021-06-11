package mp.jprime.json.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import mp.jprime.formats.DateFormat;
import mp.jprime.lang.JsonString;
import mp.jprime.lang.XmlString;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.TimeZone;

/**
 * Базовый класс JSON-обработчиков
 */
@Service
public class JPJsonMapper {
  private final ObjectMapper OBJECT_MAPPER;

  public JPJsonMapper() {
    OBJECT_MAPPER = new ObjectMapper()
        // Подключаем javaTime
        .registerModule(
            new JavaTimeModule()
                 // String to LocalDateTime
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(
                    new DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        .optionalStart()
                        .appendOffset("+HHMM", "0000")
                        .optionalEnd()
                        .toFormatter()
                ))
                // String to LocalTime
                .addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ISO_LOCAL_TIME))
                // String to LocalDate
                .addDeserializer(LocalDate.class, new LocalDateDeserializer(
                    new DateTimeFormatterBuilder()
                        .appendValue(ChronoField.YEAR, 4)
                        .appendPattern("-MM[-dd]")
                        .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                        .toFormatter()
                ))
                // String to JsonString
                .addDeserializer(JsonString.class, new StdDeserializer<JsonString>(JsonString.class) {
                  @Override
                  public JsonString deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                    return JsonString.from(p.getCodec().readTree(p).toString());
                  }
                })
                // String to XmlString
                .addDeserializer(XmlString.class, new StdDeserializer<XmlString>(XmlString.class) {
                  @Override
                  public XmlString deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                    return XmlString.from(p.getCodec().readTree(p).toString());
                  }
                })
                // LocalDateTime to String
                .addSerializer(LocalDateTime.class,
                    new JsonSerializer<LocalDateTime>() {
                      @Override
                      public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                        ZonedDateTime zdt = ZonedDateTime.of(localDateTime, TimeZone.getDefault().toZoneId());
                        jsonGenerator.writeString(DateFormat.LOCAL_DATETIME_FORMAT.format(zdt));
                      }
                    }
                )
                // LocalTime to String
                .addSerializer(LocalTime.class,
                    new JsonSerializer<LocalTime>() {
                      @Override
                      public void serialize(LocalTime localTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                        jsonGenerator.writeString(DateFormat.LOCAL_TIME_FORMAT.format(localTime));
                      }
                    }
                )
                // LocalDate to String
                .addSerializer(LocalDate.class,
                    new JsonSerializer<LocalDate>() {
                      @Override
                      public void serialize(LocalDate LocalDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                        jsonGenerator.writeString(DateFormat.LOCAL_DATE_FORMAT.format(LocalDate));
                      }
                    }
                )
                // JsonString to String
                .addSerializer(JsonString.class,
                    new JsonSerializer<JsonString>() {
                      @Override
                      public void serialize(JsonString v, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                        jsonGenerator.writeString(v.toString());
                      }
                    }
                )
                // XmlString to String
                .addSerializer(XmlString.class,
                    new JsonSerializer<XmlString>() {
                      @Override
                      public void serialize(XmlString v, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                        jsonGenerator.writeString(v.toString());
                      }
                    })
        )
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        // Игнорируем пустые значения
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        // Игнорируем переносы строк и прочие служебные символы
        .configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true)
        .addMixIn(InputStream.class, MixInForIgnoreType.class)
        .setTimeZone(TimeZone.getDefault())
        //  ISO8601
        .setDateFormat(new SimpleDateFormat(DateFormat.ISO8601));
  }

  public ObjectMapper getObjectMapper() {
    return OBJECT_MAPPER;
  }
}
