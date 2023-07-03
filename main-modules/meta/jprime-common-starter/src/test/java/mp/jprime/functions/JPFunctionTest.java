package mp.jprime.functions;

import mp.jprime.dataaccess.beans.JPMutableData;
import mp.jprime.json.services.JsonJPObjectService;
import mp.jprime.json.services.QueryService;
import org.junit.jupiter.api.Assertions;
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

import java.time.LocalDate;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JPFunctionTest.Config.class)
public class JPFunctionTest {

  @Lazy(value = false)
  @Configuration
  @ComponentScan(
      basePackages = {"mp.jprime.parsers", "mp.jprime.json.modules", "mp.jprime.json.services", "mp.jprime.functions"},
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
  private JPFunctionService functionService;

  @Test
  void testLocalDateGroup() {
    LocalDate curDate = LocalDate.now();
    LocalDate result = functionService.eval("localDateFuncs", "curDate");
    assertEquals("LocalDate is not equals", curDate, result);

    result = functionService.eval("localDateFuncs.curDate");
    assertEquals("LocalDate is not equals", curDate, result);

    result = functionService.eval("localDateFuncs", "addDay", curDate, 2);
    assertEquals("LocalDate is not equals", curDate.plusDays(2), result);

    result = functionService.eval("localDateFuncs.addDay$param1$2", JPMutableData.of("param1", curDate));
    assertEquals("LocalDate is not equals", curDate.plusDays(2), result);

    result = functionService.eval("localDateFuncs.max$param1$2000-01-01", JPMutableData.of("param1", curDate));
    assertEquals("LocalDate is not equals", curDate, result);

    result = functionService.eval("localDateFuncs.min$param1$2000-01-01", JPMutableData.of("param1", curDate));
    assertEquals("LocalDate is not equals", LocalDate.of(2000, 1, 1), result);

    result = functionService.eval("localDateFuncs.max$2001-01-01$2000-01-01");
    assertEquals("LocalDate is not equals", LocalDate.of(2001, 1, 1), result);

    result = functionService.eval("localDateFuncs", "max", "2001-01-01", "2000-01-01");
    assertEquals("LocalDate is not equals", LocalDate.of(2001, 1, 1), result);
  }

  @Test
  void testIntegerGroup() {
    Integer result = functionService.eval("integerFuncs", "sum");
    Assertions.assertEquals(0, result);

    result = functionService.eval("integerFuncs.sum$1$2$3");
    Assertions.assertEquals(6, result);
  }
}
