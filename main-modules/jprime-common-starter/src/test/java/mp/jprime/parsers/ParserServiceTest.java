package mp.jprime.parsers;

import mp.jprime.dataaccess.beans.JPId;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

import static mp.jprime.formats.DateFormat.ISO8601;

@RunWith(SpringRunner.class)
@ContextConfiguration()
public class ParserServiceTest {
  @Configuration
  @ComponentScan(value = {"mp.jprime.parsers"})
  public static class Config {
  }

  @Autowired
  private ParserService parserService;

  @Test
  public void testStringToString() {
    String in = "S";
    Object out = parserService.parseTo(String.class, in);
    Assert.assertEquals(in, out);
  }

  @Test
  public void testIntegerToInteger() {
    int in = 0;
    Object out = parserService.parseTo(Integer.class, in);
    Assert.assertEquals(in, out);
  }

  @Test
  public void testBooleanToString() {
    Boolean in = true;
    Object out = parserService.parseTo(String.class, in);
    Assert.assertEquals("true", out);
  }

  @Test
  public void testStringToBoolean() {
    String in = "1";
    Object out = parserService.parseTo(Boolean.class, in);
    Assert.assertEquals(true, out);
  }

  @Test
  public void testStringToBigInteger() {
    String in = "1";
    Object out = parserService.parseTo(BigInteger.class, in);
    Assert.assertEquals(new BigInteger("1"), out);
  }


  @Test
  public void testStringToInteger() {
    String in = "1";
    Object out = parserService.parseTo(Integer.class, in);
    Assert.assertEquals(1, out);
  }

  @Test
  public void testStringToFloat() {
    String in = "1";
    Object out = parserService.parseTo(Float.class, in);
    Assert.assertEquals(1f, out);
  }

  @Test
  public void testStringToLong() {
    String in = "56";
    Object out = parserService.parseTo(Long.class, in);
    Assert.assertEquals(56L, out);
  }

  @Test
  public void testStringToDate() throws Exception {
    String in = "2012-04-09T20:00:00.000+0000";
    Object out = parserService.parseTo(Date.class, in);
    Assert.assertEquals(new SimpleDateFormat(ISO8601).parse(in), out);
  }

  @Test
  public void testIntegerToLong() {
    Integer in = 1;
    Object out = parserService.parseTo(Long.class, in);
    Assert.assertEquals(1L, out);
  }

  @Test
  public void testLongToInteger() {
    Long in = 1L;
    Object out = parserService.parseTo(Integer.class, in);
    Assert.assertEquals(1, out);
  }

  @Test
  public void testBigIntegerToLong() {
    BigInteger in = new BigInteger("1");
    Object out = parserService.parseTo(Long.class, in);
    Assert.assertEquals(1L, out);
  }

  @Test
  public void testLongToBigInteger() {
    Long in = 1L;
    Object out = parserService.parseTo(BigInteger.class, in);
    Assert.assertEquals(new BigInteger("1"), out);
  }

  @Test
  public void testBigIntegerToInteger() {
    BigInteger in = new BigInteger("1");
    Object out = parserService.parseTo(Integer.class, in);
    Assert.assertEquals(1, out);
  }

  @Test
  public void testBigDecimalToInteger() {
    BigDecimal in = new BigDecimal("1");
    Object out = parserService.parseTo(Integer.class, in);
    Assert.assertEquals(1, out);
  }

  @Test
  public void testIntegerToBigInteger() {
    Integer in = 1;
    Object out = parserService.parseTo(BigInteger.class, in);
    Assert.assertEquals(new BigInteger("1"), out);
  }

  public void testIntegerToBigDecimal() {
    Integer in = 1;
    Object out = parserService.parseTo(BigDecimal.class, in);
    Assert.assertEquals(new BigDecimal("1"), out);
  }

  @Test
  public void testJPIdToInteger() {
    JPId in = JPId.get("test", 1);
    Object out = parserService.parseTo(Integer.class, in);
    Assert.assertEquals(1, out);
  }

  @Test
  public void testJPIdToString() {
    JPId in = JPId.get("test", "1");
    Object out = parserService.parseTo(String.class, in);
    Assert.assertEquals("1", out);
  }

  @Test
  public void testJPIdToBoolean() {
    JPId in = JPId.get("test", true);
    Object out = parserService.parseTo(Boolean.class, in);
    Assert.assertEquals(Boolean.TRUE, out);
  }

  @Test
  public void testIntegerToDouble() {
    Integer in = 1;
    Object out = parserService.parseTo(Double.class, in);
    Assert.assertEquals(1d, out);
  }

  @Test
  public void testDoubleToInteger() {
    Double in = 1d;
    Object out = parserService.parseTo(Integer.class, in);
    Assert.assertEquals(1, out);
  }

  @Test
  public void testDoubleToString() {
    Double in = 1d;
    Object out = parserService.parseTo(String.class, in);
    Assert.assertEquals("1", out);
  }

  @Test
  public void testStringToDouble() {
    String in = "1";
    Object out = parserService.parseTo(Double.class, in);
    Assert.assertEquals(1d, out);
  }

  @Test
  public void testIntegerToString() {
    Integer in = 1;
    Object out = parserService.parseTo(String.class, in);
    Assert.assertEquals("1", out);
  }

  @Test
  public void testBigIntegerToString() {
    BigInteger in = new BigInteger("1");
    Object out = parserService.parseTo(String.class, in);
    Assert.assertEquals("1", out);
  }

  @Test
  public void tesLongToString() {
    Long in = 10000000L;
    Object out = parserService.parseTo(String.class, in);
    Assert.assertEquals("10000000", out);
  }

  @Test
  public void testBooleanToInteger() {
    Boolean in = true;
    Object out = parserService.parseTo(Integer.class, in);
    Assert.assertEquals(1, out);
  }

  @Test
  public void testIntegerToBoolean() {
    Integer in = 0;
    Object out = parserService.parseTo(Boolean.class, in);
    Assert.assertEquals(Boolean.FALSE, out);

    in = 1;
    out = parserService.parseTo(Boolean.class, in);
    Assert.assertEquals(Boolean.TRUE, out);
  }
}