package mp.jprime.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JPStringUtilsTest.Config.class)
public class JPStringUtilsTest {
  @Configuration
  @ComponentScan(
      basePackages = {"mp.jprime.parsers", "mp.jprime.json.services"},
      excludeFilters = {
          @ComponentScan.Filter(
              type = FilterType.ASSIGNABLE_TYPE,
              value = {
              }
          )
      })
  @EnableConfigurationProperties
  public static class Config {
  }

  @Test
  void replaceValuesTest() {
    String result = JPStringUtils.replaceAttrValues(
        "{ATTR_VALUE.f1} + {ATTR_VALUE.f2} + {ATTR_VALUE.f1} + {ATTR_VALUE.f3}",
        s -> 1);
    Assertions.assertEquals("1 + 1 + 1 + 1", result);

    result = JPStringUtils.replaceParamValues(
        "{PARAMS.f1} + {PARAMS.f2} + {PARAMS.f1} + {PARAMS.f3}",
        s -> 1);
    Assertions.assertEquals("1 + 1 + 1 + 1", result);

    result = JPStringUtils.replaceParamValues("""
           {
             "cond": {
               "attr": "decisionDate",
               "eqMonth": "{PARAMS.year}-{PARAMS.month}-1"
             }
           }""",
        s -> 1);
    Assertions.assertEquals("""
        {
          "cond": {
            "attr": "decisionDate",
            "eqMonth": "1-1-1"
          }
        }""", result);
  }

  @Test
  void applyDataTimeTemplateTest() {
    LocalDateTime dateTime = LocalDateTime.of(2025, 2, 25, 6, 13);
    String dateTimeStr = JPStringUtils.applyDataTimeTemplate("#day#/#hour#/#minute#", dateTime);
    Assertions.assertEquals("25/06/13", dateTimeStr);
  }

  @Test
  void validStringCode() {
    Assertions.assertTrue(JPStringUtils.isCurrentCode("myCode"));
    Assertions.assertTrue(JPStringUtils.isCurrentCode("123"));
    Assertions.assertTrue(JPStringUtils.isCurrentCode("myCode_123-abc.def"));

    Assertions.assertFalse(JPStringUtils.isCurrentCode(null));
    Assertions.assertFalse(JPStringUtils.isCurrentCode(""));
    Assertions.assertFalse(JPStringUtils.isCurrentCode("мойКод"));
    Assertions.assertFalse(JPStringUtils.isCurrentCode("%"));
  }

  @Test
  void removeNonDigitTest() {
    Assertions.assertEquals("123321", JPStringUtils.removeNonDigit("123-321"));
  }

  @Test
  void testBankCardNumber() {
    Assertions.assertTrue(JPStringUtils.isValidBankCardNumber("2255 2109 7015 9748")); // верный по алг Луна
    Assertions.assertTrue(JPStringUtils.isValidBankCardNumber("2200 3307 0424 5345 444")); // верный по алг Луна
    Assertions.assertFalse(JPStringUtils.isValidBankCardNumber("2255 2109 7015 9747")); // не верный по алг Луна
    Assertions.assertFalse(JPStringUtils.isValidBankCardNumber("2255 2109 7015 9747 1234"));
    Assertions.assertFalse(JPStringUtils.isValidBankCardNumber("2255 210t 7015 9748"));
    Assertions.assertFalse(JPStringUtils.isValidBankCardNumber("2255 21"));
    Assertions.assertFalse(JPStringUtils.isValidBankCardNumber("5"));
    Assertions.assertFalse(JPStringUtils.isValidBankCardNumber("2r"));
    Assertions.assertFalse(JPStringUtils.isValidBankCardNumber(null));
  }
}
