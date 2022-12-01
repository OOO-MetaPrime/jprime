package mp.jprime.json.modules;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import mp.jprime.json.beans.*;
import mp.jprime.lang.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.regex.Pattern;

/**
 * Подключение базовых обработчиков
 */
@Service
public final class JPObjectMapperCommonExpander implements JPObjectMapperExpander {
  public final static long MAX_SAFE_INTEGER = 0x000FFFFFFFFFFFFFL * 2 + 1;
  public final static long MIN_SAFE_INTEGER = -1 * MAX_SAFE_INTEGER;

  public final static BigDecimal MAX_SAFE_BIGDECIMAL = BigDecimal.valueOf(MAX_SAFE_INTEGER);
  public final static BigDecimal MIN_SAFE_BIGDECIMAL = BigDecimal.valueOf(MIN_SAFE_INTEGER);

  public final static BigInteger MAX_SAFE_BIGINTEGER = BigInteger.valueOf(MAX_SAFE_INTEGER);
  public final static BigInteger MIN_SAFE_BIGINTEGER = BigInteger.valueOf(MIN_SAFE_INTEGER);

  /**
   * Прекомпилированный шаблон замены
   */
  private static final Pattern PATTERN = Pattern.compile(" ", Pattern.LITERAL);

  @Override
  public void expand(ObjectMapper objectMapper) {
    SimpleModule module = new SimpleModule()
        // String to Double
        .addDeserializer(Double.class, new StdDeserializer<Double>(Double.class) {
          @Override
          public Double deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String value = p.getText();
            value = value != null ? value.trim() : null;
            if (value == null || value.isEmpty()) {
              return null;
            }
            value = value.replace(',', '.');
            return Double.valueOf(value.indexOf(' ') > -1 ? PATTERN.matcher(value).replaceAll("") : value);
          }
        })
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
        );
    objectMapper.registerModule(module);
  }
}