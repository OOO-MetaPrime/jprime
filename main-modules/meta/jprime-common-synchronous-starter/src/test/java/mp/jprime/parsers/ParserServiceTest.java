package mp.jprime.parsers;

import mp.jprime.dataaccess.beans.JPId;
import mp.jprime.json.services.JsonJPObjectService;
import mp.jprime.json.services.QueryService;
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

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ParserServiceTest.Config.class)
class ParserServiceTest {

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
}