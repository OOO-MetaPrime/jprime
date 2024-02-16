package mp.jprime.parsers;

import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.json.services.JsonJPObjectService;
import mp.jprime.json.services.QueryService;
import mp.jprime.lang.JPJsonNode;
import mp.jprime.lang.JPJsonString;
import mp.jprime.lang.JPXmlString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.TimeZone;
import java.util.UUID;

import static mp.jprime.formats.DateFormat.ISO8601;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ParserServiceTest.Config.class)
class ParserServiceTest {
  private static final String TEST_DATE_FORMAT = "yyyy-MM-dd";
  private static final String TEST_DATE_TIME_FORMAT = TEST_DATE_FORMAT + " HH:mm:ss.SSS";
  private static final String TEST_DATE = "2021-07-21";
  private static final String TEST_TIME = "10:06:40.023";
  private static final String TEST_DATE_TIME = TEST_DATE + " " + TEST_TIME;
  private static final String TEST_DATE_TIME_S = TEST_DATE + "T" + TEST_TIME + "+0300";
  private static final String TEST_JSON = "{\"code\":1}";
  private static final String TEST_XML = "<employees>" +
      "   <employee id=\"101\">" +
      "        <name>Uncle Bob</name>" +
      "       <title>Author</title>" +
      "   </employee>" +
      "</employees>";

  @Lazy(value = false)
  @Configuration
  @ComponentScan(
      basePackages = {"mp.jprime.parsers", "mp.jprime.json.services"},
      excludeFilters = {
          @ComponentScan.Filter(
              type = FilterType.ASSIGNABLE_TYPE,
              value = {
                  JsonJPObjectService.class,
                  QueryService.class
              }
          )
      })
  @EnableConfigurationProperties
  public static class Config {
  }

  @Autowired
  private ParserService parserService;

  @Test
  void testStringToString() {
    String in = "S";
    Object out = parserService.parseTo(String.class, in);
    assertEquals(in, out);
  }

  @Test
  void testIntegerToInteger() {
    int in = 0;
    Object out = parserService.parseTo(Integer.class, in);
    assertEquals(in, out);
  }

  @Test
  void testBooleanToString() {
    Boolean in = true;
    Object out = parserService.parseTo(String.class, in);
    assertEquals("true", out);
  }

  @Test
  void testStringToBoolean() {
    String in = "1";
    Object out = parserService.parseTo(Boolean.class, in);
    assertEquals(Boolean.TRUE, out);
  }

  @Test
  void testStringToBooleanFalse() {
    String in = "f3dsd";
    Object out = parserService.parseTo(Boolean.class, in);
    assertEquals(Boolean.FALSE, out);
  }

  @Test
  void testStringToBigInteger() {
    String in = "1";
    Object out = parserService.parseTo(BigInteger.class, in);
    assertEquals(new BigInteger("1"), out);
  }

  @Test
  void testStringToInteger() {
    String in = "1";
    Object out = parserService.parseTo(Integer.class, in);
    assertEquals(1, out);
  }

  @Test
  void testStringToLong() {
    String in = "56";
    Object out = parserService.parseTo(Long.class, in);
    assertEquals(56L, out);
  }

  @Test
  void testStringToDate() throws Exception {
    Object out = parserService.parseTo(Date.class, TEST_DATE_TIME_S);
    assertEquals(new SimpleDateFormat(ISO8601).parse(TEST_DATE_TIME_S), out);
  }

  @Test
  void testIntegerToLong() {
    Integer in = 1;
    Object out = parserService.parseTo(Long.class, in);
    assertEquals(1L, out);
  }

  @Test
  void testLongToInteger() {
    Long in = 1L;
    Object out = parserService.parseTo(Integer.class, in);
    assertEquals(1, out);
  }

  @Test
  void testLongToLong() {
    Long in = 1L;
    Object out = parserService.parseTo(Long.class, in);
    assertEquals(1L, out);
  }

  @Test
  void testBigIntegerToLong() {
    BigInteger in = new BigInteger("1");
    Object out = parserService.parseTo(Long.class, in);
    assertEquals(1L, out);
  }

  @Test
  void testLongToBigInteger() {
    Long in = 1L;
    Object out = parserService.parseTo(BigInteger.class, in);
    assertEquals(new BigInteger("1"), out);
  }

  @Test
  void testLongToDouble() {
    Long in = 1L;
    Object out = parserService.parseTo(Double.class, in);
    assertEquals(1d, out);
  }

  @Test
  void testBigIntegerToInteger() {
    BigInteger in = new BigInteger("1");
    Object out = parserService.parseTo(Integer.class, in);
    assertEquals(1, out);
  }

  @Test
  void testBigIntegerToBigInteger() {
    BigInteger in = new BigInteger("1");
    Object out = parserService.parseTo(BigInteger.class, in);
    assertEquals(new BigInteger("1"), out);
  }

  @Test
  void testBigDecimalToDouble() {
    BigDecimal in = new BigDecimal("1");
    Object out = parserService.parseTo(Double.class, in);
    assertEquals(1.0, out);
  }

  @Test
  void testDoubleToBigDecimal() {
    Double in = 1d;
    Object out = parserService.parseTo(BigDecimal.class, in);
    assertEquals(new BigDecimal(1d), out);
  }

  @Test
  void testBigDecimalToInteger() {
    BigDecimal in = new BigDecimal("1");
    Object out = parserService.parseTo(Integer.class, in);
    assertEquals(1, out);
  }

  @Test
  void testIntegerToBigInteger() {
    Integer in = 1;
    Object out = parserService.parseTo(BigInteger.class, in);
    assertEquals(new BigInteger("1"), out);
  }

  @Test
  void testIntegerToBigDecimal() {
    Integer in = 1;
    Object out = parserService.parseTo(BigDecimal.class, in);
    assertEquals(new BigDecimal("1"), out);
  }

  @Test
  void testJPIdToInteger() {
    JPId in = JPId.get("test", 1);
    Object out = parserService.parseTo(Integer.class, in);
    assertEquals(1, out);
  }

  @Test
  void testJPIdToString() {
    JPId in = JPId.get("test", "1");
    Object out = parserService.parseTo(String.class, in);
    assertEquals("1", out);
  }

  @Test
  void testJPIdToBoolean() {
    JPId in = JPId.get("test", true);
    Object out = parserService.parseTo(Boolean.class, in);
    assertEquals(Boolean.TRUE, out);
  }

  @Test
  void testIntegerToDouble() {
    Integer in = 1;
    Object out = parserService.parseTo(Double.class, in);
    assertEquals(1d, out);
  }

  @Test
  void testDoubleToInteger() {
    Double in = 1d;
    Object out = parserService.parseTo(Integer.class, in);
    assertEquals(1, out);
  }

  @Test
  void testDoubleToLong() {
    Double in = 1d;
    Object out = parserService.parseTo(Long.class, in);
    assertEquals(1L, out);
  }

  @Test
  void testIntegerToFloat() {
    Integer in = 1;
    Object out = parserService.parseTo(Float.class, in);
    assertEquals(1f, out);
  }

  @Test
  void testFloatToInteger() {
    Float in = 1f;
    Object out = parserService.parseTo(Integer.class, in);
    assertEquals(1, out);
  }

  @Test
  void testDoubleToString() {
    Double in = 1d;
    Object out = parserService.parseTo(String.class, in);
    assertEquals("1", out);
  }

  @Test
  void testStringToDouble() {
    String in = "1";
    Object out = parserService.parseTo(Double.class, in);
    assertEquals(1d, out);
  }

  @Test
  void testFloatToString() {
    Float in = 1f;
    Object out = parserService.parseTo(String.class, in);
    assertEquals("1.0", out);
  }

  @Test
  void testStringToFloat() {
    String in = "1";
    Object out = parserService.parseTo(Float.class, in);
    assertEquals(1f, out);
  }

  @Test
  void testIntegerToString() {
    Integer in = 1;
    Object out = parserService.parseTo(String.class, in);
    assertEquals("1", out);
  }

  @Test
  void testBigIntegerToString() {
    BigInteger in = new BigInteger("1");
    Object out = parserService.parseTo(String.class, in);
    assertEquals("1", out);
  }

  @Test
  void tesLongToString() {
    Long in = 10000000L;
    Object out = parserService.parseTo(String.class, in);
    assertEquals("10000000", out);
  }

  @Test
  void testBooleanToInteger() {
    Boolean in = true;
    Object out = parserService.parseTo(Integer.class, in);
    assertEquals(1, out);
  }

  @Test
  void testIntegerToBoolean() {
    Integer in = 0;
    Object out = parserService.parseTo(Boolean.class, in);
    assertEquals(Boolean.FALSE, out);
    in = 1;
    out = parserService.parseTo(Boolean.class, in);
    assertEquals(Boolean.TRUE, out);
  }

  @Test
  void testBooleanToBoolean() {
    Boolean in = true;
    Object out = parserService.parseTo(Boolean.class, in);
    assertEquals(Boolean.TRUE, out);
  }

  @Test
  void testDateToLocalDate() throws ParseException {
    Date in = new SimpleDateFormat(TEST_DATE_FORMAT).parse(TEST_DATE);
    LocalDate out = parserService.parseTo(LocalDate.class, in);
    assertEquals(LocalDate.parse(TEST_DATE), out);
  }

  @Test
  void testDateToSqlDate() throws ParseException {
    Date in = new SimpleDateFormat(TEST_DATE_FORMAT).parse(TEST_DATE);
    java.sql.Date out = parserService.parseTo(java.sql.Date.class, in);
    assertEquals(java.sql.Date.valueOf(TEST_DATE), out);
  }

  @Test
  void testDateToTimestamp() throws ParseException {
    Date in = new SimpleDateFormat(TEST_DATE_FORMAT).parse(TEST_DATE);
    Object out = parserService.parseTo(Timestamp.class, in);
    assertEquals(new Timestamp(in.getTime()), out);
  }

  @Test
  void testJPIdToBigInteger() {
    JPId in = JPId.get("", 1);
    Object out = parserService.parseTo(BigInteger.class, in);
    assertEquals(new BigInteger("1"), out);
  }

  @Test
  void testJPIdToDouble() {
    JPId in = JPId.get("", 1d);
    Object out = parserService.parseTo(Double.class, in);
    assertEquals(1d, out);
  }

  @Test
  void testJPIdToFloat() {
    JPId in = JPId.get("", 1f);
    Object out = parserService.parseTo(Float.class, in);
    assertEquals(1f, out);
  }

  @Test
  void testJPIdToIntegerParser() {
    JPId in = JPId.get("", 1);
    Object out = parserService.parseTo(Integer.class, in);
    assertEquals(1, out);
  }

  @Test
  void testJPIdToLong() {
    JPId in = JPId.get("", 1L);
    Object out = parserService.parseTo(Long.class, in);
    assertEquals(1L, out);
  }

  @Test
  void testJsonStringToString() {
    JPJsonString in = JPJsonString.from(TEST_JSON);
    Object out = parserService.parseTo(String.class, in);
    assertEquals(TEST_JSON, out);
  }

  @Test
  void testLocalDateToDate() {
    LocalDate in = LocalDate.parse(TEST_DATE);
    Date out = parserService.parseTo(Date.class, in);
    assertEquals(Date.from(in.atStartOfDay(ZoneId.systemDefault()).toInstant()), out);
  }

  @Test
  void testLocalDateToLocalDateTime() {
    LocalDateTime in = LocalDateTime.parse(TEST_DATE_TIME_S, DateTimeFormatter.ofPattern(ISO8601));
    Object out = parserService.parseTo(LocalDateTime.class, in);
    assertEquals(LocalDateTime.parse(TEST_DATE_TIME_S, DateTimeFormatter.ofPattern(ISO8601)), out);
  }

  @Test
  void testLocalDateToString() {
    LocalDate in = LocalDate.parse(TEST_DATE);
    Object out = parserService.parseTo(String.class, in);
    assertEquals(TEST_DATE, out);
  }

  @Test
  void testLocalTimeToString() {
    LocalTime in = LocalTime.parse(TEST_TIME);
    Object out = parserService.parseTo(String.class, in);
    assertEquals(TEST_TIME, out);
  }

  @Test
  void testSqlDateToDate() throws ParseException {
    java.sql.Date in = java.sql.Date.valueOf(TEST_DATE);
    Object out = parserService.parseTo(Date.class, in);
    assertEquals(new SimpleDateFormat(TEST_DATE_FORMAT).parse(TEST_DATE), out);
  }

  @Test
  void testSqlDateToLocalDate() {
    java.sql.Date in = java.sql.Date.valueOf(TEST_DATE);
    Object out = parserService.parseTo(LocalDate.class, in);
    assertEquals(LocalDate.parse(TEST_DATE), out);
  }

  @Test
  void testSqlDateToLocalDateTime() {
    java.sql.Date in = java.sql.Date.valueOf(TEST_DATE);
    Object out = parserService.parseTo(LocalDateTime.class, in);
    assertEquals(LocalDate.parse(TEST_DATE, DateTimeFormatter.ofPattern(TEST_DATE_FORMAT)).atStartOfDay(), out);
  }

  @Test
  void testSqlDateToTimestamp() {
    java.sql.Date in = java.sql.Date.valueOf(TEST_DATE);
    Object out = parserService.parseTo(Timestamp.class, in);
    assertEquals(new Timestamp(in.getTime()), out);
  }

  @Test
  void testStringToJsonString() {
    JPJsonString out = parserService.parseTo(JPJsonString.class, TEST_JSON);
    assertEquals(JPJsonString.from(TEST_JSON).toString(), out.toString());
  }

  @Test
  void testStringToLocalDate() {
    Object out = parserService.parseTo(LocalDate.class, TEST_DATE);
    assertEquals(LocalDate.parse(TEST_DATE), out);
  }

  @Test
  void testStringToLocalDateTime() {
    Object out = parserService.parseTo(LocalDateTime.class, TEST_DATE_TIME_S);
    LocalDateTime expected = ZonedDateTime.parse(TEST_DATE_TIME_S, DateTimeFormatter.ofPattern(ISO8601))
        .withZoneSameInstant(TimeZone.getDefault().toZoneId()).toLocalDateTime();
    assertEquals(expected, out);
  }

  @Test
  void testStringToLocalTime() {
    Object out = parserService.parseTo(LocalTime.class, TEST_TIME);
    assertEquals(LocalTime.parse(TEST_TIME), out);
  }

  @Test
  void testStringToUUID() {
    String in = "f219326c-4a9a-4b17-84c0-ddd135fe7da9";
    Object out = parserService.parseTo(UUID.class, in);
    assertEquals(UUID.fromString(in), out);
  }

  @Test
  void testUUIDToString() {
    UUID in = UUID.fromString("f219326c-4a9a-4b17-84c0-ddd135fe7da9");
    Object out = parserService.parseTo(String.class, in);
    assertEquals(in.toString(), out);
  }

  @Test
  void testTimestampToDate() throws ParseException {
    Timestamp in = Timestamp.valueOf(TEST_DATE_TIME);
    Object out = parserService.parseTo(Date.class, in);
    assertEquals(new SimpleDateFormat(TEST_DATE_TIME_FORMAT).parse(TEST_DATE_TIME), out);
  }

  @Test
  void testXmlStringToString() {
    JPXmlString in = JPXmlString.from(TEST_XML);
    Object out = parserService.parseTo(String.class, in);
    assertEquals(in.toString(), out);
  }

  @Test
  void testStringToXmlString() {
    Object out = parserService.parseTo(JPXmlString.class, TEST_XML);
    assertEquals(JPXmlString.from(TEST_XML).toString(), out.toString());
  }

  @Test
  void testJpJsonNodeToString() {
    Object in = parserService.parseTo(JPJsonNode.class, TEST_JSON);
    Object out = parserService.parseTo(String.class, in);
    assertEquals(out, TEST_JSON);
  }

  @Test
  void testLinkedHashMapToJPJsonString() {
    LinkedHashMap<String, Object> in = new LinkedHashMap<>();
    in.put("key1", 1);
    in.put("key2", "2");

    JPJsonString out = parserService.parseTo(JPJsonString.class, in);
    assertEquals(out.toString(), "{\"key1\":1,\"key2\":\"2\"}");
  }
}