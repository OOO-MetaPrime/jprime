package mp.jprime.json.modules;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import mp.jprime.formats.DateFormat;
import mp.jprime.xml.modules.JPObjectMapperXmlExpander;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.TimeZone;

/**
 * Подключение JavaTimeModule
 */
@Service
public final class JPObjectMapperJavaTimeExpander implements JPObjectMapperExpander, JPObjectMapperXmlExpander {
  @Override
  public void expand(ObjectMapper objectMapper) {
    objectMapper.registerModule(
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
                    .optionalStart()
                    .appendZoneId()
                    .optionalEnd()
                    .toFormatter()
            ))
            // LocalDateTime to String
            .addSerializer(LocalDateTime.class,
                new JsonSerializer<>() {
                  @Override
                  public void serialize(LocalDateTime localDateTime, JsonGenerator jGen, SerializerProvider sProv) throws IOException {
                    ZonedDateTime zdt = ZonedDateTime.of(localDateTime, TimeZone.getDefault().toZoneId());
                    jGen.writeString(DateFormat.LOCAL_DATETIME_FORMAT.format(zdt));
                  }
                }
            )
            // LocalTime to String
            .addSerializer(LocalTime.class,
                new JsonSerializer<>() {
                  @Override
                  public void serialize(LocalTime localTime, JsonGenerator jGen, SerializerProvider sProv) throws IOException {
                    jGen.writeString(DateFormat.LOCAL_TIME_FORMAT.format(localTime));
                  }
                }
            )
            // LocalDate to String
            .addSerializer(LocalDate.class,
                new JsonSerializer<>() {
                  @Override
                  public void serialize(LocalDate LocalDate, JsonGenerator jGen, SerializerProvider sProv) throws IOException {
                    jGen.writeString(DateFormat.LOCAL_DATE_FORMAT.format(LocalDate));
                  }
                }
            )
    );
  }
}
