package mp.jprime.json.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import mp.jprime.exceptions.JPRuntimeException;
import mp.jprime.formats.DateFormat;
import mp.jprime.json.beans.*;
import mp.jprime.lang.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
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
  public final static long MAX_SAFE_INTEGER = sun.misc.DoubleConsts.SIGNIF_BIT_MASK * 2 + 1;
  public final static long MIN_SAFE_INTEGER = -1 * MAX_SAFE_INTEGER;

  public final static BigDecimal MAX_SAFE_BIGDECIMAL = BigDecimal.valueOf(MAX_SAFE_INTEGER);
  public final static BigDecimal MIN_SAFE_BIGDECIMAL =  BigDecimal.valueOf(MIN_SAFE_INTEGER);

  public final static BigInteger MAX_SAFE_BIGINTEGER = BigInteger.valueOf(MAX_SAFE_INTEGER);
  public final static BigInteger MIN_SAFE_BIGINTEGER =  BigInteger.valueOf(MIN_SAFE_INTEGER);

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
                .addDeserializer(JPJsonString.class, new StdDeserializer<JPJsonString>(JPJsonString.class) {
                  @Override
                  public JPJsonString deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                    return JPJsonString.from(p.getCodec().readTree(p).toString());
                  }
                })
                // String to XmlString
                .addDeserializer(JPXmlString.class, new StdDeserializer<JPXmlString>(JPXmlString.class) {
                  @Override
                  public JPXmlString deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                    return JPXmlString.from(p.getCodec().readTree(p).toString());
                  }
                })
                // String to JPSimpleFraction
                .addDeserializer(JPSimpleFraction.class, new StdDeserializer<JPSimpleFraction>(JPSimpleFraction.class) {
                  @Override
                  public JPSimpleFraction deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                    JsonSimpleFraction fraction = ctxt.readValue(p, JsonSimpleFraction.class);
                    return JPSimpleFraction.of(fraction.getPositive(), fraction.getInteger(), fraction.getNumerator(), fraction.getDenominator());
                  }
                })
                // String to Money
                .addDeserializer(JPMoney.class, new StdDeserializer<JPMoney>(JPMoney.class) {
                  @Override
                  public JPMoney deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                    JsonMoney money = ctxt.readValue(p, JsonMoney.class);
                    return JPMoney.of(money.getValue(), money.getCurrencyCode());
                  }
                })
                // String to IntegerRange
                .addDeserializer(JPIntegerRange.class, new StdDeserializer<JPIntegerRange>(JPIntegerRange.class) {
                  @Override
                  public JPIntegerRange deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                    JsonIntegerRange range = ctxt.readValue(p, JsonIntegerRange.class);
                    return JPIntegerRange.create(range.getLower(), range.getUpper());
                  }
                })
                // String to DateRange
                .addDeserializer(JPDateRange.class, new StdDeserializer<JPDateRange>(JPDateRange.class) {
                  @Override
                  public JPDateRange deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                    JsonDateRange range = ctxt.readValue(p, JsonDateRange.class);
                    return JPDateRange.create(range.getLower(), range.getUpper());
                  }
                })
                // String to DateTimeRange
                .addDeserializer(JPDateTimeRange.class, new StdDeserializer<JPDateTimeRange>(JPDateTimeRange.class) {
                  @Override
                  public JPDateTimeRange deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                    JsonDateTimeRange range = ctxt.readValue(p, JsonDateTimeRange.class);
                    return JPDateTimeRange.create(range.getLower(), range.getUpper(), range.isCloseLower(), range.isCloseUpper());
                  }
                })
                // String to BigDecimal
                .addDeserializer(BigDecimal.class, new StdDeserializer<BigDecimal>(BigDecimal.class) {
                  @Override
                  public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                    String s = p.getValueAsString();
                    return StringUtils.hasText(s) ? new BigDecimal(s) : null;
                  }
                })
                // JsonNode to JPJsonNode
                .addDeserializer(JPJsonNode.class, new StdDeserializer<JPJsonNode>(JPJsonNode.class) {
                  @Override
                  public JPJsonNode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                    return JPJsonNode.from(p.getCodec().readTree(p));
                  }
                })
                // LocalDateTime to String
                .addSerializer(LocalDateTime.class,
                    new JsonSerializer<LocalDateTime>() {
                      @Override
                      public void serialize(LocalDateTime localDateTime, JsonGenerator jGen, SerializerProvider sProv) throws IOException {
                        ZonedDateTime zdt = ZonedDateTime.of(localDateTime, TimeZone.getDefault().toZoneId());
                        jGen.writeString(DateFormat.LOCAL_DATETIME_FORMAT.format(zdt));
                      }
                    }
                )
                // LocalTime to String
                .addSerializer(LocalTime.class,
                    new JsonSerializer<LocalTime>() {
                      @Override
                      public void serialize(LocalTime localTime, JsonGenerator jGen, SerializerProvider sProv) throws IOException {
                        jGen.writeString(DateFormat.LOCAL_TIME_FORMAT.format(localTime));
                      }
                    }
                )
                // LocalDate to String
                .addSerializer(LocalDate.class,
                    new JsonSerializer<LocalDate>() {
                      @Override
                      public void serialize(LocalDate LocalDate, JsonGenerator jGen, SerializerProvider sProv) throws IOException {
                        jGen.writeString(DateFormat.LOCAL_DATE_FORMAT.format(LocalDate));
                      }
                    }
                )
                // JsonString to String
                .addSerializer(JPJsonString.class,
                    new JsonSerializer<JPJsonString>() {
                      @Override
                      public void serialize(JPJsonString v, JsonGenerator jGen, SerializerProvider sProv) throws IOException {
                        jGen.writeString(v.toString());
                      }
                    }
                )
                // XmlString to String
                .addSerializer(JPXmlString.class,
                    new JsonSerializer<JPXmlString>() {
                      @Override
                      public void serialize(JPXmlString v, JsonGenerator jGen, SerializerProvider sProv) throws IOException {
                        jGen.writeString(v.toString());
                      }
                    })
                // JPSimpleFraction to String
                .addSerializer(JPSimpleFraction.class,
                    new JsonSerializer<JPSimpleFraction>() {
                      @Override
                      public void serialize(JPSimpleFraction v, JsonGenerator jGen, SerializerProvider sProv) throws IOException {
                        JsonSimpleFraction json = new JsonSimpleFraction();
                        json.setPositive(v.isPositive());
                        json.setInteger(v.getInteger());
                        json.setNumerator(v.getNumerator());
                        json.setDenominator(v.getDenominator());
                        jGen.writeObject(json);
                      }
                    })
                // Money to String
                .addSerializer(JPMoney.class,
                    new JsonSerializer<JPMoney>() {
                      @Override
                      public void serialize(JPMoney v, JsonGenerator jGen, SerializerProvider sProv) throws IOException {
                        JsonMoney json = new JsonMoney();
                        json.setValue(v.getNumberStripped());
                        json.setCurrencyCode(v.getCurrencyCode());
                        jGen.writeObject(json);
                      }
                    })
                // IntegerRange to String
                .addSerializer(JPIntegerRange.class,
                    new JsonSerializer<JPIntegerRange>() {
                      @Override
                      public void serialize(JPIntegerRange v, JsonGenerator jGen, SerializerProvider sProv) throws IOException {
                        JsonIntegerRange json = new JsonIntegerRange();
                        json.setLower(v.lower());
                        json.setUpper(v.upper());
                        jGen.writeObject(json);
                      }
                    })
                // DateRange to String
                .addSerializer(JPDateRange.class,
                    new JsonSerializer<JPDateRange>() {
                      @Override
                      public void serialize(JPDateRange v, JsonGenerator jGen, SerializerProvider sProv) throws IOException {
                        JsonDateRange json = new JsonDateRange();
                        json.setLower(v.lower());
                        json.setUpper(v.upper());
                        jGen.writeObject(json);
                      }
                    })
                // DateTimeRange to String
                .addSerializer(JPDateTimeRange.class,
                    new JsonSerializer<JPDateTimeRange>() {
                      @Override
                      public void serialize(JPDateTimeRange v, JsonGenerator jGen, SerializerProvider sProv) throws IOException {
                        JsonDateTimeRange json = new JsonDateTimeRange();
                        json.setLower(v.lower());
                        json.setUpper(v.upper());
                        json.setCloseLower(v.isLowerBoundClosed());
                        json.setCloseUpper(v.isUpperBoundClosed());
                        jGen.writeObject(json);
                      }
                    })
                // BigDecimal to String
                .addSerializer(BigDecimal.class,
                    new JsonSerializer<BigDecimal>() {
                      @Override
                      public void serialize(BigDecimal v, JsonGenerator jGen, SerializerProvider sProv) throws IOException {
                        // При длине > 16 символов браузер игнорирует другие цифры
                        if (v.precision() > 16 || v.compareTo(MIN_SAFE_BIGDECIMAL) < 0 || v.compareTo(MAX_SAFE_BIGDECIMAL) > 0) {
                          jGen.writeString(v.toString());
                        } else {
                          jGen.writeNumber(v);
                        }
                      }
                    })
                // Long to String
                .addSerializer(Long.class,
                    new JsonSerializer<Long>() {
                      @Override
                      public void serialize(Long v, JsonGenerator jGen, SerializerProvider sProv) throws IOException {
                        if (v < MIN_SAFE_INTEGER || v > MAX_SAFE_INTEGER) {
                          jGen.writeString(v.toString());
                        } else {
                          jGen.writeNumber(v);
                        }
                      }
                    })
                // BigInteger to String
                .addSerializer(BigInteger.class,
                    new JsonSerializer<BigInteger>() {
                      @Override
                      public void serialize(BigInteger v, JsonGenerator jGen, SerializerProvider sProv) throws IOException {
                        if (v.compareTo(MIN_SAFE_BIGINTEGER) < 0 || v.compareTo(MAX_SAFE_BIGINTEGER) > 0) {
                          jGen.writeString(v.toString());
                        } else {
                          jGen.writeNumber(v);
                        }
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
                )
        )
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
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

  public String toString(Object object) {
    if (object == null) {
      return null;
    }
    try {
      return getObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw JPRuntimeException.wrapException(e);
    }
  }
}
