package mp.jprime.json.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import mp.jprime.formats.DateFormat;
import mp.jprime.json.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public interface JsonMapper {
  Logger LOG = LoggerFactory.getLogger(JsonMapper.class);

  ObjectMapper OBJECT_MAPPER = new ObjectMapper()
      // Подключаем javaTime
      .registerModule(
          new JavaTimeModule()
              // String to Date
              .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(
                  new DateTimeFormatterBuilder()
                      .parseCaseInsensitive()
                      .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                      .optionalStart()
                      .appendOffset("+HHMM", "0000")
                      .optionalEnd()
                      .toFormatter()
              ))
              .addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ISO_LOCAL_TIME))
              .addDeserializer(LocalDate.class, new LocalDateDeserializer(
                  new DateTimeFormatterBuilder()
                      .appendValue(ChronoField.YEAR, 4)
                      .appendPattern("-MM[-dd]")
                      .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                      .toFormatter()
              ))
              // Date to String
              .addSerializer(LocalDateTime.class,
                  new JsonSerializer<LocalDateTime>() {
                    @Override
                    public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                      ZonedDateTime zdt = ZonedDateTime.of(localDateTime, TimeZone.getDefault().toZoneId());
                      jsonGenerator.writeString(DateTimeFormat.LOCAL_DATETIME_FORMAT.format(zdt));
                    }
                  }
              )
              .addSerializer(LocalTime.class,
                  new JsonSerializer<LocalTime>() {
                    @Override
                    public void serialize(LocalTime localTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                      jsonGenerator.writeString(DateTimeFormat.LOCAL_TIME_FORMAT.format(localTime));
                    }
                  }
              )
              .addSerializer(LocalDate.class,
                  new JsonSerializer<LocalDate>() {
                    @Override
                    public void serialize(LocalDate LocalDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                      jsonGenerator.writeString(DateTimeFormat.LOCAL_DATE_FORMAT.format(LocalDate));
                    }
                  }
              )
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
