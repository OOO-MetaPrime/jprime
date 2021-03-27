package mp.jprime.parsers;

import mp.jprime.dataaccess.beans.JPId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

import static mp.jprime.formats.DateFormat.ISO8601;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration()
class ParserServiceTest {
  @Lazy(value = false)
  @Configuration
  @ComponentScan(value = {"mp.jprime.parsers"})
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
    assertEquals(true, out);
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
    String in = "2012-04-09T20:00:00.000+0000";
    Object out = parserService.parseTo(Date.class, in);
    assertEquals(new SimpleDateFormat(ISO8601).parse(in), out);
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
  void testBigIntegerToInteger() {
    BigInteger in = new BigInteger("1");
    Object out = parserService.parseTo(Integer.class, in);
    assertEquals(1, out);
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
}