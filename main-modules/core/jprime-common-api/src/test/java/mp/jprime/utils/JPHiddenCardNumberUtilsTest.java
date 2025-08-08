package mp.jprime.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JPHiddenCardNumberUtilsTest {

  @Test
  void testHiddenCardNumber() {
    Assertions.assertTrue(JPHiddenCardNumberUtils.isHiddenCardNumber("*1234"));
    Assertions.assertTrue(JPHiddenCardNumberUtils.isHiddenCardNumber("************1234"));
    Assertions.assertTrue(JPHiddenCardNumberUtils.isHiddenCardNumber("* 1234"));
    Assertions.assertTrue(JPHiddenCardNumberUtils.isHiddenCardNumber("**** **** **** 1234"));
    Assertions.assertFalse(JPHiddenCardNumberUtils.isHiddenCardNumber("**** **** **** 123"));
    Assertions.assertFalse(JPHiddenCardNumberUtils.isHiddenCardNumber("1234 1234 1234 1234"));
    Assertions.assertFalse(JPHiddenCardNumberUtils.isHiddenCardNumber("*123"));
    Assertions.assertFalse(JPHiddenCardNumberUtils.isHiddenCardNumber("1234"));
    Assertions.assertFalse(JPHiddenCardNumberUtils.isHiddenCardNumber(null));
  }

  @Test
  void testGetHiddenCardNumber() {
    Assertions.assertEquals("************9748", JPHiddenCardNumberUtils.getHiddenCardNumber("2255 2109 7015 9748"));
    Assertions.assertEquals("***************5444", JPHiddenCardNumberUtils.getHiddenCardNumber("2200 3307 0424 5345 444"));
    Assertions.assertEquals("************9748", JPHiddenCardNumberUtils.getHiddenCardNumber("2255210970159748"));
    Assertions.assertNull(JPHiddenCardNumberUtils.getHiddenCardNumber(null));
  }
}
