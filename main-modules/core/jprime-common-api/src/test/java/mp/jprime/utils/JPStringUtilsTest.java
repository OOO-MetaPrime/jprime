package mp.jprime.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class JPStringUtilsTest {
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
