package mp.jprime.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JPStringUtilsTest {
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
}
